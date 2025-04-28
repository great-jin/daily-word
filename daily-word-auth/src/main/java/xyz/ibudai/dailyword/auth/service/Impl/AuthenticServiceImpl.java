package xyz.ibudai.dailyword.auth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.basic.consts.RegexConst;
import xyz.ibudai.dailyword.basic.tool.CodeTool;
import xyz.ibudai.dailyword.basic.tool.RegexTool;
import xyz.ibudai.dailyword.model.dto.PasswordDTO;
import xyz.ibudai.dailyword.model.entity.user.AuthUser;
import xyz.ibudai.dailyword.auth.service.AuthenticService;
import xyz.ibudai.dailyword.model.entity.InviteCode;
import xyz.ibudai.dailyword.model.entity.user.UserDetail;
import xyz.ibudai.dailyword.model.enums.EmailType;
import xyz.ibudai.dailyword.model.enums.status.EmailCodeStatus;
import xyz.ibudai.dailyword.model.enums.status.InviteCodeStatus;
import xyz.ibudai.dailyword.model.enums.status.PasswordStatus;
import xyz.ibudai.dailyword.model.enums.status.RegisterStatus;
import xyz.ibudai.dailyword.model.props.TemplateProps;
import xyz.ibudai.dailyword.model.vo.RegisterVo;
import xyz.ibudai.dailyword.repository.dao.AuthUserDao;
import xyz.ibudai.dailyword.repository.dao.InviteCodeDao;
import xyz.ibudai.dailyword.repository.dao.UserDetailDao;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    private static final String PLACE_HOLDER_OPERATE = "aaaaaa";
    private static final String PLACE_HOLDER_CODE = "bbbbbb";

    /**
     * 缓存邮件验证码
     */
    private final static Cache<String, String> EMAIL_CODE_MAP = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .initialCapacity(16)
            .build();

    /**
     * 记录上次发送时间
     */
    private final static Cache<String, LocalDateTime> EMAIL_TIME_MAP = Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .initialCapacity(16)
            .build();


    @Value("${spring.mail.username}")
    private String emailFrom;

    private final TemplateProps templateProps;

    private final JavaMailSender mailSender;

    private final AuthUserDao authUserDao;
    private final UserDetailDao userDetailDao;
    private final InviteCodeDao inviteCodeDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserDao.queryByName(username);
        if (authUser == null) {
            throw new IllegalArgumentException("User [" + username + "] doesn't exist.");
        }
        return authUser;
    }

    @Override
    public EmailCodeStatus validateEmail(Integer type, String address) {
        Matcher matcher = RegexConst.PATTERN_EMAIL.matcher(address);
        if (!matcher.matches()) {
            return EmailCodeStatus.EMAIL_INVALID;
        }

        QueryWrapper<UserDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("email", address);
        boolean emailInUse = Objects.nonNull(userDetailDao.selectOne(wrapper));

        // 不允许邮箱重复注册
        if (emailInUse && Objects.equals(type, EmailType.Register.getCode())) {
            return EmailCodeStatus.EMAIL_IN_USE;
        }

        // 忘记密码需匹配邮箱
        if (!emailInUse && Objects.equals(type, EmailType.Forgot.getCode())) {
            return EmailCodeStatus.EMAIL_NOT_REGISTER;
        }

        return EmailCodeStatus.SUCCESS;
    }

    @Override
    public EmailCodeStatus sendMail(Integer type, String address) {
        String emailKey = UUID.nameUUIDFromBytes((type + address).getBytes()).toString();

        // 拦截短频尝试
        LocalDateTime latestSendTime = EMAIL_TIME_MAP.getIfPresent(emailKey);
        if (Objects.nonNull(latestSendTime)) {
            long between = Math.abs(
                    Duration.between(latestSendTime, LocalDateTime.now())
                            .toMinutes()
            );
            if (between <= 60) {
                // 60 秒内不允许重复发送
                return EmailCodeStatus.REQ_TO_MANY;
            }
        }

        // 生成并记录验证码
        if (StringUtils.isNotBlank(EMAIL_CODE_MAP.getIfPresent(emailKey))) {
            // 旧值存在则废除
            EMAIL_CODE_MAP.invalidate(emailKey);
        }
        String mailCode = CodeTool.generate();
        EMAIL_CODE_MAP.put(emailKey, mailCode);

        EmailType emailType = EmailType.getByType(type);
        if (Objects.isNull(emailType)) {
            log.error("The email type of {} illegal.", type);
            return EmailCodeStatus.FAIL;
        }

        // 发送验证码
        EmailCodeStatus codeStatus;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(emailFrom);
            helper.setTo(address);
            helper.setSubject("Daily Word 验证码");

            // 解析处理模板
            String template = Files.readString(Paths.get(templateProps.getEmail()));
            template = template.replace(PLACE_HOLDER_OPERATE, emailType.getMode());
            template = template.replace(PLACE_HOLDER_CODE, mailCode);
            helper.setText(template, true);

            // 发件邮件
            mailSender.send(mimeMessage);
            codeStatus = EmailCodeStatus.SUCCESS;

            // 成功记录发送时间
            EMAIL_TIME_MAP.put(emailKey, LocalDateTime.now());
        } catch (Exception e) {
            log.error("Mail sending failed.", e);
            codeStatus = EmailCodeStatus.FAIL;
        }
        return codeStatus;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterStatus register(RegisterVo registerVo) {
        String key = UUID.nameUUIDFromBytes(
                (EmailType.Register.getCode() + registerVo.getEmail()).getBytes()
        ).toString();
        String code = EMAIL_CODE_MAP.getIfPresent(key);
        if (StringUtils.isBlank(code) || !Objects.equals(code, registerVo.getCaptcha())) {
            // 验证码无效
            return RegisterStatus.EMAIL_CODE_INVALID;
        }

        String username = registerVo.getUsername();
        if (!RegexTool.isAlphaNumeric(username)) {
            // username illegal
            return RegisterStatus.USERNAME_INVALID;
        }
        List<AuthUser> list = authUserDao.selectList(
                new QueryWrapper<AuthUser>()
                        .eq("user_name", username)
        );
        if (!CollectionUtils.isEmpty(list)) {
            // Username has been used
            return RegisterStatus.NAME_USED;
        }

        // 创建登录账号
        AuthUser authUser = AuthUser.initUser();
        authUser.setUsername(username);
        authUser.setPassword(registerVo.getPassword());
        boolean userSaved = authUserDao.insert(authUser) > 0;

        // 创建用户信息
        Integer userId = authUser.getId();
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(userId);
        userDetail.setUserName(registerVo.getUsername());
        userDetail.setEmail(registerVo.getEmail());
        userDetail.setInviteCode(registerVo.getInviteCode());
        boolean detailSaved = userDetailDao.insert(userDetail) > 0;

        // 更新邀请码为无效
        inviteCodeDao.update(
                new UpdateWrapper<InviteCode>()
                        .set("status", InviteCodeStatus.USED.getStatus())
                        .eq("code", registerVo.getInviteCode())
        );

        // 注册成功移除验证码
        RegisterStatus status = userSaved && detailSaved
                ? RegisterStatus.SUCCESS
                : RegisterStatus.FAILED;
        if (status.equals(RegisterStatus.SUCCESS)) {
            EMAIL_CODE_MAP.invalidate(key);
            EMAIL_TIME_MAP.invalidate(key);
        }
        return status;
    }

    @Override
    public PasswordStatus forgot(PasswordDTO dto) {
        String key = UUID.nameUUIDFromBytes(
                (EmailType.Forgot.getCode() + dto.getEmail()).getBytes()
        ).toString();
        String code = EMAIL_CODE_MAP.getIfPresent(key);
        if (StringUtils.isBlank(code) || !Objects.equals(code, dto.getCaptcha())) {
            // 验证码无效
            return PasswordStatus.CAPTCHA_MISMATCH;
        }

        // 检查邮箱注册状态
        UserDetail userDetail = userDetailDao.selectOne(
                new QueryWrapper<UserDetail>()
                        .eq("email", dto.getEmail())
        );
        if (Objects.isNull(userDetail)) {
            return PasswordStatus.EMAIL_NOT_REGISTER;
        }

        // 修改密码
        UpdateWrapper<AuthUser> wrapper = new UpdateWrapper<>();
        wrapper.set("password", dto.getPassword());
        wrapper.eq("id", userDetail.getUserId());
        boolean success = authUserDao.update(wrapper) > 0;
        return success
                ? PasswordStatus.SUCCESS
                : PasswordStatus.FAILED;
    }
}
