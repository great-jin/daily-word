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

    /**
     * Security 登录请求接口
     */
    @PostMapping("login")
    public void login(AuthUser user) {
    }

    @GetMapping("sendMail")
    public Boolean sendMail(@RequestParam("mail") String address) {
        return authUserService.sendMail(address);
    }

    @PostMapping("register")
    public Boolean register(@RequestBody AuthUser user) {
        return authUserService.register(user);
    }

    @PostMapping("forgot")
    public Boolean forgot(@RequestBody AuthUser user) {
        return authUserService.forgot(user);
    }
}
