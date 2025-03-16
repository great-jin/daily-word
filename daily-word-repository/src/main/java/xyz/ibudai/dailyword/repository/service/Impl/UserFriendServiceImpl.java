package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.model.entity.UserFriend;
import xyz.ibudai.dailyword.model.vo.UserFriendVo;
import xyz.ibudai.dailyword.repository.dao.AuthUserDao;
import xyz.ibudai.dailyword.repository.dao.UserFriendDao;
import xyz.ibudai.dailyword.repository.service.UserFriendService;

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
    private AuthUserDao authUserDao;


    @Override
    public List<UserFriendVo> findByUserId(Integer userId) {
        List<Integer> userIdList = this.baseMapper.findLinkUser(userId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Collections.emptyList();
        }

        QueryWrapper<AuthUser> wrapper = new QueryWrapper<AuthUser>()
                .select("id", "user_name")
                .in("id", userIdList);
        List<AuthUser> authUsers = authUserDao.selectList(wrapper);
        List<UserFriendVo> result = new ArrayList<>();
        for (AuthUser user : authUsers) {
            result.add(new UserFriendVo(user.getId(), user.getUsername(), true));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Integer userId, Integer friendId) {
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

