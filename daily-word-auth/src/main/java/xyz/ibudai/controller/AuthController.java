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
    @PostMapping("verify")
    public void authVerify(AuthUser user) {
    }

    @PostMapping("login")
    public boolean login(AuthUser user) throws Exception {
        return authUserService.login(user);
    }
}
