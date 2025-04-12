package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.vo.friend.UserFriendVo;
import xyz.ibudai.dailyword.server.service.UserFriendService;

import java.util.List;

/**
 * (UserFriend)表控制层
 *
 * @author ibudai
 * @since 2025-03-16 09:26:05
 */
@RestController
@RequestMapping("/api/server/userFriend")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserFriendResource {

    private final UserFriendService userFriendService;


    /**
     * List data.
     *
     * @return the list
     */
    @GetMapping("list")
    public List<UserFriendVo> list() {
        return userFriendService.findByUserId();
    }

    /**
     * Delete by id boolean.
     *
     * @param friendId the friend id
     * @return the list
     */
    @GetMapping("deleteById")
    public Boolean deleteById(@RequestParam("friendId") Integer friendId) {
        return userFriendService.deleteById(friendId);
    }
}

