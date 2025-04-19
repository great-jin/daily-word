package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.enums.InviteType;
import xyz.ibudai.dailyword.model.vo.friend.FriendInviteVo;
import xyz.ibudai.dailyword.server.service.UserInvitationService;

import java.util.List;

/**
 * (UserInvitation)表控制层
 *
 * @author budai
 * @since 2025 -04-12 09:02:23
 */
@RestController
@RequestMapping("/api/server/userInvite")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInvitationResource {

    private final UserInvitationService userInvitationService;

    /**
     * Count.
     *
     * @return the integer
     */
    @GetMapping("count")
    public Integer count() {
        return this.listReqs(InviteType.toMe).size();
    }

    /**
     * Search friend invite vo.
     *
     * @param username the username
     * @return the friend invite vo
     */
    @GetMapping("search")
    public FriendInviteVo search(@RequestParam("username") String username) {
        return userInvitationService.search(username);
    }

    /**
     * List.
     *
     * @param inviteType the type
     * @return the list
     */
    @GetMapping("list")
    public List<FriendInviteVo> listReqs(@RequestParam("inviteType") InviteType inviteType) {
        return userInvitationService.listReqs(inviteType);
    }

    /**
     * Send invite.
     *
     * @param userId the user id
     * @return the boolean
     */
    @GetMapping("sendInvite")
    public Boolean sendInvite(@RequestParam("userId") Integer userId) {
        return userInvitationService.sendInvite(userId);
    }

    /**
     * Handle request.
     *
     * @param userId the user id
     * @param status the status
     * @return the boolean
     */
    @GetMapping("handleInvite")
    public Boolean handleInvite(@RequestParam("userId") Integer userId,
                                @RequestParam("status") Integer status) {
        return userInvitationService.handleInvite(userId, status);
    }
}

