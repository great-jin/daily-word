package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.model.entity.UserFriend;
import xyz.ibudai.dailyword.model.vo.UserFriendVo;
import xyz.ibudai.dailyword.repository.dao.AuthUserDao;
import xyz.ibudai.dailyword.repository.dao.UserFriendDao;
import xyz.ibudai.dailyword.repository.service.AuthUserService;
import xyz.ibudai.dailyword.repository.service.UserFriendService;

import java.sql.Wrapper;
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
}

