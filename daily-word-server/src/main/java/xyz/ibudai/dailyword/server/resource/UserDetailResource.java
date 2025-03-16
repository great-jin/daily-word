package xyz.ibudai.dailyword.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.entity.UserDetail;
import xyz.ibudai.dailyword.repository.dao.UserDetailDao;

/**
 * (UserDetail)表控制层
 *
 * @author ibudai
 * @since 2025-03-16 09:26:04
 */
@RestController
@RequestMapping("/api/server/userDetail")
public class UserDetailResource {

    @Autowired
    private UserDetailDao userDetailDao;


    @GetMapping("{userId}")
    public UserDetail getById(@PathVariable("userId") Integer userId) {
        return userDetailDao.selectById(userId);
    }
}

