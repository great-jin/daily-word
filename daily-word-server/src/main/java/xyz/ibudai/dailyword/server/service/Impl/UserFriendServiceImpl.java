package xyz.ibudai.dailyword.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.basic.tool.DateTimeTool;
import xyz.ibudai.dailyword.model.entity.user.UserDetail;
import xyz.ibudai.dailyword.model.entity.user.UserFriend;
import xyz.ibudai.dailyword.model.vo.friend.UserFriendVo;
import xyz.ibudai.dailyword.oss.util.OssServer;
import xyz.ibudai.dailyword.repository.dao.UserFriendDao;
import xyz.ibudai.dailyword.auth.util.SecurityUtil;
import xyz.ibudai.dailyword.server.service.UserDetailService;
import xyz.ibudai.dailyword.server.service.UserFriendService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * (UserFriend)表服务实现类
 *
 * @author budai
 * @since 2025-03-16 09:26:05
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserFriendServiceImpl extends ServiceImpl<UserFriendDao, UserFriend> implements UserFriendService {

    private final OssServer ossServer;

    private final UserDetailService userDetailService;


    @Override
    public List<UserFriendVo> findByUserId() {
        List<Integer> userIdList = this.baseMapper.findLinkUser(SecurityUtil.getLoginUser());
        if (CollectionUtils.isEmpty(userIdList)) {
            return Collections.emptyList();
        }

        List<UserDetail> list = userDetailService.lambdaQuery()
                .in(UserDetail::getUserId, userIdList)
                .list();
        // 构建前端实体
        List<UserFriendVo> result = new ArrayList<>();
        LocalDateTime nowadays = LocalDateTime.now();
        for (UserDetail user : list) {
            String avatarUrl = ossServer.signAvatarUrl(user.getAvatar());
            UserFriendVo friendVo = UserFriendVo.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .online(false)
                    .avatarUrl(avatarUrl)
                    .lastOnline(DateTimeTool.interval(user.getLastOnlineTime(), nowadays))
                    .build();
            result.add(friendVo);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Integer friendId) {
        Integer userId = SecurityUtil.getLoginUser();
        List<Integer> ids = new ArrayList<>();
        // 用户添加的好友
        List<UserFriend> list1 = this.lambdaQuery()
                .eq(UserFriend::getUserId, userId)
                .eq(UserFriend::getFriendId, friendId)
                .list();
        if (!CollectionUtils.isEmpty(list1)) {
            ids.addAll(list1.stream().map(UserFriend::getId).toList());
        }
        // 用户被添加的好友
        List<UserFriend> list2 = this.lambdaQuery()
                .eq(UserFriend::getUserId, friendId)
                .eq(UserFriend::getFriendId, userId)
                .list();
        if (!CollectionUtils.isEmpty(list2)) {
            ids.addAll(list2.stream().map(UserFriend::getId).toList());
        }

        boolean success = false;
        if (!ids.isEmpty()) {
            success = this.baseMapper.deleteByIds(ids) > 0;
        }
        return success;
    }
}

