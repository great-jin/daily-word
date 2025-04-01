package xyz.ibudai.dailyword.auth.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.auth.service.AuthenticService;
import xyz.ibudai.dailyword.model.entity.InviteCode;
import xyz.ibudai.dailyword.model.entity.UserDetail;
import xyz.ibudai.dailyword.model.vo.RegisterVo;
import xyz.ibudai.dailyword.repository.service.InviteCodeService;

import java.util.List;

/**
 * The type Login resource.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticResource {

    private final AuthenticService authenticService;
    private final InviteCodeService inviteCodeService;


    /**
     * Security 登录请求接口
     *
     * @param user the user
     */
    @PostMapping("login")
    public void login(AuthUser user) {
    }

    /**
     * Valida code boolean.
     *
     * @param inviteCode the invite code
     * @return the boolean
     */
    @GetMapping("validaCode")
    public Boolean validaCode(@RequestParam("inviteCode") String inviteCode) {
        List<InviteCode> list = inviteCodeService.lambdaQuery()
                .eq(InviteCode::getCode, inviteCode)
                .eq(InviteCode::getActive, Boolean.TRUE)
                .list();
        return !CollectionUtils.isEmpty(list);
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
     * @param registerVo the user
     * @return the boolean
     */
    @PostMapping("register")
    public Integer register(@RequestBody RegisterVo registerVo) {
        return authenticService.register(registerVo);
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
