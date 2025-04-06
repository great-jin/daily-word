package xyz.ibudai.dailyword.auth.service.Impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.auth.util.CodeUtil;
import xyz.ibudai.dailyword.basic.consts.RegexConst;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.auth.service.AuthenticService;
import xyz.ibudai.dailyword.model.entity.InviteCode;
import xyz.ibudai.dailyword.model.entity.UserDetail;
import xyz.ibudai.dailyword.model.enums.RegisterStatus;
import xyz.ibudai.dailyword.model.vo.RegisterVo;
import xyz.ibudai.dailyword.repository.service.AuthUserService;
import xyz.ibudai.dailyword.repository.service.InviteCodeService;
import xyz.ibudai.dailyword.repository.service.UserDetailService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

/**
 * (TbUser)表服务实现类
 *
 * @author budai
 * @since 2023 -01-31 14:31:28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticServiceImpl implements AuthenticService {

    private final static Cache<String, String> VERIFY_CODE_MAP = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .initialCapacity(100)
            .maximumSize(1000)
            .build();

    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender mailSender;

    private final AuthUserService authUserService;
    private final UserDetailService userDetailService;
    private final InviteCodeService inviteCodeService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserService.queryByName(username);
        if (authUser == null) {
            throw new IllegalArgumentException("User [" + username + "] doesn't exist.");
        }
        return authUser;
    }

    @Override
    public boolean sendMail(String address) {
        Matcher matcher = RegexConst.PATTERN_EMAIL.matcher(address);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Email is Illegal.");
        }
        List<UserDetail> list = userDetailService.lambdaQuery()
                .eq(UserDetail::getEmail, address)
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException("Email has been registered.");
        }

        // 生成并记录验证码
        String mailCode;
        String key = UUID.nameUUIDFromBytes(address.getBytes()).toString();
        String code = VERIFY_CODE_MAP.getIfPresent(key);
        if (!StringUtils.isBlank(code)) {
            mailCode = code;
        } else {
            mailCode = CodeUtil.generateEmailCode();
            VERIFY_CODE_MAP.put(key, mailCode);
        }
        // 发送验证码
        boolean success;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(address);
            message.setSubject("Daily Word 邮箱验证码");
            message.setText("您的验证码为：" + mailCode);
            mailSender.send(message);
            success = true;
        } catch (Exception e) {
            success = false;
            log.error("Mail sending failed.", e);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer register(RegisterVo registerVo) {
        List<AuthUser> list = authUserService.lambdaQuery()
                .eq(AuthUser::getUsername, registerVo.getUsername())
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            // Username has been used
            return RegisterStatus.NAME_USED.getCode();
        }

        // 创建登录账号
        AuthUser authUser = AuthUser.initUser();
        authUser.setUsername(registerVo.getUsername());
        authUser.setPassword(registerVo.getPassword());
        boolean userSaved = authUserService.save(authUser);

        // 创建用户信息
        Integer userId = authUser.getId();
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(userId);
        userDetail.setUserName(registerVo.getUsername());
        userDetail.setEmail(registerVo.getEmail());
        userDetail.setInviteCode(registerVo.getInviteCode());
        boolean detailSaved = userDetailService.save(userDetail);

        // 更新邀请码为无效
        inviteCodeService.lambdaUpdate()
                .set(InviteCode::getActive, false)
                .eq(InviteCode::getCode, registerVo.getInviteCode())
                .update();

        return userSaved && detailSaved
                ? RegisterStatus.SUCCESS.getCode()
                : RegisterStatus.FAILED.getCode();
    }

    @Override
    public boolean forgot(UserDetail user) {
        return false;
    }
}
