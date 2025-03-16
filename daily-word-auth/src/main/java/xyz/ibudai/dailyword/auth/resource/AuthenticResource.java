package xyz.ibudai.dailyword.auth.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.auth.service.AuthenticService;
import xyz.ibudai.dailyword.model.entity.UserDetail;

/**
 * The type Login resource.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticResource {

    @Autowired
    private AuthenticService authenticService;


    /**
     * Security 登录请求接口
     *
     * @param user the user
     */
    @PostMapping("login")
    public void login(AuthUser user) {
    }

    /**
     * Send mail boolean.
     *
     * @param address the address
     * @return the boolean
     */
    @GetMapping("sendMail")
    public Boolean sendMail(@RequestParam("mail") String address) {
        return authenticService.sendMail(address);
    }

    /**
     * Register boolean.
     *
     * @param user the user
     * @return the boolean
     */
    @PostMapping("register")
    public Boolean register(@RequestBody UserDetail user) {
        return authenticService.register(user);
    }

    /**
     * Forgot boolean.
     *
     * @param user the user
     * @return the boolean
     */
    @PostMapping("forgot")
    public Boolean forgot(@RequestBody UserDetail user) {
        return authenticService.forgot(user);
    }
}
