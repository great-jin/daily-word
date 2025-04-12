package xyz.ibudai.dailyword.auth.resource;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.entity.user.AuthUser;
import xyz.ibudai.dailyword.auth.service.AuthenticService;
import xyz.ibudai.dailyword.model.entity.InviteCode;
import xyz.ibudai.dailyword.model.entity.user.UserDetail;
import xyz.ibudai.dailyword.model.enums.InviteCodeStatus;
import xyz.ibudai.dailyword.model.vo.RegisterVo;
import xyz.ibudai.dailyword.repository.dao.AuthUserDao;
import xyz.ibudai.dailyword.repository.dao.InviteCodeDao;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Login resource.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticResource {

    private final InviteCodeDao inviteCodeDao;

    private final AuthUserDao authUserDao;
    private final AuthenticService authenticService;


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
        QueryWrapper<InviteCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", inviteCode);
        queryWrapper.eq("status", InviteCodeStatus.UN_USE.getStatus());
        // 过期时间大于当前日期
        queryWrapper.ge("expire_date", LocalDateTime.now());
        List<InviteCode> list = inviteCodeDao.selectList(queryWrapper);
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

    /**
     * Destroy boolean.
     *
     * @return the boolean
     */
    @GetMapping("destroy")
    public Boolean destroy() {
        UpdateWrapper<AuthUser> wrapper = new UpdateWrapper<>();
        wrapper.set("is_enabled", false);
        wrapper.eq("id", SecurityUtil.getLoginUser());
        return authUserDao.update(wrapper) > 0;
    }
}
