package xyz.ibudai.dailyword.server.service.Impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.entity.user.UserDetail;
import xyz.ibudai.dailyword.model.entity.user.UserFriend;
import xyz.ibudai.dailyword.model.entity.user.UserInvitation;
import xyz.ibudai.dailyword.model.enums.status.InviteProcessStatus;
import xyz.ibudai.dailyword.model.enums.InviteType;
import xyz.ibudai.dailyword.model.vo.friend.FriendInviteVo;
import xyz.ibudai.dailyword.oss.util.OssServer;
import xyz.ibudai.dailyword.repository.dao.UserFriendDao;
import xyz.ibudai.dailyword.repository.dao.UserInvitationDao;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;
import xyz.ibudai.dailyword.server.service.UserDetailService;
import xyz.ibudai.dailyword.server.service.UserInvitationService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * (UserInvitation)表服务实现类
 *
 * @author makejava
 * @since 2025-04-12 09:02:23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInvitationServiceImpl extends ServiceImpl<UserInvitationDao, UserInvitation> implements UserInvitationService {

    private final OssServer ossServer;

    private final UserFriendDao userFriendDao;
    private final UserDetailService userDetailService;


    @Override
    public FriendInviteVo search(String username) {
        UserDetail userDetail = userDetailService.lambdaQuery()
                .eq(UserDetail::getUserName, username)
                .one();
        if (Objects.isNull(userDetail)) {
            throw new IllegalStateException("用户 " + username + " 不存在");
        }
        Integer loginUser = SecurityUtil.getLoginUser();
        if (Objects.equals(loginUser, userDetail.getUserId())) {
            throw new IllegalStateException("不能添加您自己为好友");
        }
        List<UserInvitation> list = this.lambdaQuery()
                .eq(UserInvitation::getFromUser, loginUser)
                .eq(UserInvitation::getTargetUser, userDetail.getUserId())
                .eq(UserInvitation::getProcessStatus, InviteProcessStatus.Pending.getStatus())
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            throw new IllegalStateException("您已请求此用户，请勿重复添加");
        }
        Set<Integer> friendVos = new HashSet<>(userFriendDao.findLinkUser(SecurityUtil.getLoginUser()));
        if (friendVos.contains(userDetail.getUserId())) {
            throw new IllegalStateException(username + " 已是您的好友");
        }

        FriendInviteVo vo = new FriendInviteVo();
        vo.setUserId(userDetail.getUserId());
        vo.setUsername(userDetail.getUserName());
        vo.setAvatarUrl(ossServer.signAvatarUrl(userDetail.getAvatar()));
        return vo;
    }

    @Override
    public List<FriendInviteVo> listReqs(InviteType type) {
        // 条件构建
        LambdaQueryChainWrapper<UserInvitation> wrapper = this.lambdaQuery();
        if (Objects.equals(type, InviteType.toMe)) {
            // 待处理的邀请
            wrapper.eq(UserInvitation::getProcessStatus, InviteProcessStatus.Pending.getStatus());
            wrapper.eq(UserInvitation::getTargetUser, SecurityUtil.getLoginUser());
        } else {
            // 发出的邀请
            wrapper.eq(UserInvitation::getFromUser, SecurityUtil.getLoginUser());
        }
        // 查询 7 天内请求
        wrapper.ge(UserInvitation::getCreateTime, LocalDateTime.now().minusDays(7));
        List<UserInvitation> list = wrapper.list();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        // 查询用户信息
        List<Integer> userIds = list.stream()
                .map(it -> Objects.equals(type, InviteType.toMe) ? it.getFromUser() : it.getTargetUser())
                .toList();
        Map<Integer, UserDetail> userDetailMap = userDetailService.groupByUserId(userIds);

        // 构建 VO
        List<FriendInviteVo> voList = new ArrayList<>();
        for (UserInvitation item : list) {
            UserDetail userDetail;
            if (Objects.equals(type, InviteType.toMe)) {
                userDetail = userDetailMap.get(item.getFromUser());
            } else {
                userDetail = userDetailMap.get(item.getTargetUser());
            }
            if (Objects.isNull(userDetail)) {
                continue;
            }

            FriendInviteVo vo = new FriendInviteVo();
            vo.setUserId(userDetail.getUserId());
            vo.setUsername(userDetail.getUserName());
            vo.setAvatarUrl(ossServer.signAvatarUrl(userDetail.getAvatar()));
            vo.setProcessStatus(item.getProcessStatus());
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public Boolean handleInvite(Integer userId, Integer status) {
        Integer loginUser = SecurityUtil.getLoginUser();
        boolean update = this.lambdaUpdate()
                .set(UserInvitation::getProcessStatus, status)
                .eq(UserInvitation::getFromUser, userId)
                .eq(UserInvitation::getTargetUser, loginUser)
                .update();

        // 同意邀请
        if (status.equals(InviteProcessStatus.Ok.getStatus())) {
            UserFriend userFriend = new UserFriend();
            userFriend.setUserId(userId);
            userFriend.setFriendId(loginUser);
            userFriendDao.insert(userFriend);
        }
        return update;
    }
}

