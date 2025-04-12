package xyz.ibudai.dailyword.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.ibudai.dailyword.model.entity.user.UserFriend;

import java.util.List;

/**
 * (UserFriend)表数据库访问层
 *
 * @author budai
 * @since 2025-03-16 09:26:05
 */
public interface UserFriendDao extends BaseMapper<UserFriend> {

    List<Integer> findLinkUser(Integer userId);
}

