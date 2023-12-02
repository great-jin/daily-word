package xyz.ibudai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.entity.AuthUser;
import xyz.ibudai.service.AuthUserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthUserService authUserService;

    @PostMapping("register")
    public boolean register(@RequestBody AuthUser user) throws Exception {
        return authUserService.login(user);
    }

    /**
     * Security 登录请求接口
     */
    @PostMapping("login")
    public void login(AuthUser user) {
    }

    @PostMapping("forgot")
    public boolean forgot(@RequestBody AuthUser user) throws Exception {
        return authUserService.login(user);
    }

    @PostMapping("test")
    public boolean test() {
        return true;
    }
}
