package xyz.ibudai.dailyword.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.entity.user.UserInvitation;
import xyz.ibudai.dailyword.model.enums.InviteType;
import xyz.ibudai.dailyword.model.vo.friend.FriendInviteVo;

import java.util.List;

/**
 * (UserInvitation)表服务接口
 *
 * @author budai
 * @since 2025 -04-12 09:02:23
 */
public interface UserInvitationService extends IService<UserInvitation> {

    /**
     * Search friend invite vo.
     *
     * @param username the username
     * @return the friend invite vo
     */
    FriendInviteVo search(String username);

    /**
     * Send invite.
     *
     * @param userId the user id
     */
    Boolean sendInvite(Integer userId);

    /**
     * List from me reqs list.
     *
     * @param type the type
     * @return the list
     */
    List<FriendInviteVo> listReqs(InviteType type);

    /**
     * Handle invite boolean.
     *
     * @param userId the user id
     * @param status the status
     * @return the boolean
     */
    Boolean handleInvite(Integer userId, Integer status);
}

