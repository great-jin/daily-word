package xyz.ibudai.dailyword.auth.resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.base.ResponseData;
import xyz.ibudai.dailyword.model.dto.PasswordDTO;
import xyz.ibudai.dailyword.model.entity.user.AuthUser;
import xyz.ibudai.dailyword.auth.service.AuthenticService;
import xyz.ibudai.dailyword.model.entity.InviteCode;
import xyz.ibudai.dailyword.model.enums.status.EmailCodeStatus;
import xyz.ibudai.dailyword.model.enums.status.InviteCodeStatus;
import xyz.ibudai.dailyword.model.enums.status.PasswordStatus;
import xyz.ibudai.dailyword.model.enums.status.RegisterStatus;
import xyz.ibudai.dailyword.model.vo.RegisterVo;
import xyz.ibudai.dailyword.repository.dao.AuthUserDao;
import xyz.ibudai.dailyword.repository.dao.InviteCodeDao;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
     * @param type  the type
     * @param email the address
     * @return the boolean
     */
    @GetMapping("sendMail")
    public ResponseData sendMail(@RequestParam("type") Integer type,
                                 @RequestParam("email") String email) {
        EmailCodeStatus status = authenticService.sendMail(type, email);
        if (!Objects.equals(status, EmailCodeStatus.SUCCESS)) {
            return ResponseData.failed(status.getMessage());
        }

        return ResponseData.success(true);
    }

    /**
     * Register boolean.
     *
     * @param registerVo the user
     * @return the boolean
     */
    @PostMapping("register")
    public ResponseData register(@RequestBody RegisterVo registerVo) {
        RegisterStatus status = authenticService.register(registerVo);
        if (!Objects.equals(status, RegisterStatus.SUCCESS)) {
            return ResponseData.failed(status.getMessage());
        }

        return ResponseData.success(true);
    }

    /**
     * Forgot boolean.
     *
     * @param dto the dto
     * @return the boolean
     */
    @PostMapping("forgot")
    public ResponseData forgot(@RequestBody PasswordDTO dto) {
        PasswordStatus status = authenticService.forgot(dto);
        if (!Objects.equals(status, PasswordStatus.SUCCESS)) {
            return ResponseData.failed(status.getMessage());
        }

        return ResponseData.success(true);
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
