package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.model.entity.UserFriend;
import xyz.ibudai.dailyword.model.vo.UserFriendVo;
import xyz.ibudai.dailyword.repository.dao.UserFriendDao;
import xyz.ibudai.dailyword.repository.service.AuthUserService;
import xyz.ibudai.dailyword.repository.service.UserFriendService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * (UserFriend)表服务实现类
 *
 * @author makejava
 * @since 2025-03-16 09:26:05
 */
@Service
public class UserFriendServiceImpl extends ServiceImpl<UserFriendDao, UserFriend> implements UserFriendService {

    @Autowired
    private AuthUserService authUserService;


    @Override
    public List<UserFriendVo> findByUserId() {
        List<Integer> userIdList = this.baseMapper.findLinkUser(SecurityUtil.getLoginUser());
        if (CollectionUtils.isEmpty(userIdList)) {
            return Collections.emptyList();
        }

        List<AuthUser> authUsers = authUserService.lambdaQuery()
                .select(AuthUser::getId, AuthUser::getUsername)
                .in(AuthUser::getId, userIdList)
                .list();
        // 构建前端实体
        List<UserFriendVo> result = new ArrayList<>();
        for (AuthUser user : authUsers) {
            UserFriendVo friendVo = UserFriendVo.builder()
                    .userId(user.getId())
                    .userName(user.getUsername())
                    .online(false)
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

