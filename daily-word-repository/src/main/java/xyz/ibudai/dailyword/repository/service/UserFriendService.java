package xyz.ibudai.dailyword.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.model.entity.UserFriend;
import xyz.ibudai.dailyword.model.vo.UserFriendVo;

import java.util.List;

/**
 * (UserFriend)表服务接口
 *
 * @author makejava
 * @since 2025-03-16 09:26:05
 */
public interface UserFriendService extends IService<UserFriend> {

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<UserFriendVo> findByUserId(Integer userId);
}

