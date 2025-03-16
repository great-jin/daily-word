package xyz.ibudai.dailyword.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.model.entity.UserFriend;
import xyz.ibudai.dailyword.model.vo.UserFriendVo;
import xyz.ibudai.dailyword.repository.service.UserFriendService;

import java.util.List;

/**
 * (UserFriend)表控制层
 *
 * @author ibudai
 * @since 2025-03-16 09:26:05
 */
@RestController
@RequestMapping("/api/server/userFriend")
public class UserFriendResource {

    /**
     * 服务对象
     */
    @Autowired
    private UserFriendService userFriendService;


    /**
     * List data.
     *
     * @param userId the user id
     * @return the list
     */
    @GetMapping("list")
    public List<UserFriendVo> list(Integer userId) {
        return userFriendService.findByUserId(userId);
    }
}

