package xyz.ibudai.dailyword.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.entity.user.UserFriend;
import xyz.ibudai.dailyword.model.vo.friend.UserFriendVo;

import java.util.List;

/**
 * (UserFriend)表服务接口
 *
 * @author budai
 * @since 2025-03-16 09:26:05
 */
public interface UserFriendService extends IService<UserFriend> {

    /**
     * Find by user id list.
     *
     * @return the list
     */
    List<UserFriendVo> findByUserId();

    /**
     * 双向删除用户关系
     *
     * @param friendId the friend id
     * @return the boolean
     */
    Boolean deleteById(Integer friendId);
}

