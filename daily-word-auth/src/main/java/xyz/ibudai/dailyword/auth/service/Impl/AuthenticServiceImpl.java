package xyz.ibudai.dailyword.auth.service.Impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.auth.service.AuthenticService;
import xyz.ibudai.dailyword.auth.util.AESUtil;
import xyz.ibudai.dailyword.repository.service.UserService;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (TbUser)表服务实现类
 *
 * @author makejava
 * @since 2023 -01-31 14:31:28
 */
@Slf4j
@Service
public class AuthenticServiceImpl implements AuthenticService {

    private final static Cache<String, String> verifyCode = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .initialCapacity(100)
            .maximumSize(1000)
            .build();

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = userService.queryByName(username);
        if (authUser == null) {
            throw new IllegalArgumentException("User [" + username + "] doesn't exist.");
        }
        return authUser;
    }

    @Override
    public boolean login(AuthUser user) {
        try {
            AuthUser dbUser = userService.queryByName(user.getUsername());
            if (dbUser == null) {
                throw new IllegalAccessException("User [" + user.getUsername() + "] doesn't exist.");
            }

            String userPwd = user.getPassword();
            String dbUserPwd = AESUtil.desEncrypt(dbUser.getPassword()).trim();
            return Objects.equals(userPwd, dbUserPwd);
        } catch (Exception e) {
            log.error("登录验证异常", e);
            throw new RuntimeException("Login verify failed.", e);
        }
    }

    @Override
    public boolean sendMail(String address) {
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(address);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Email is Illegal.");
        }

        String mailCode;
        String key = UUID.nameUUIDFromBytes(address.getBytes()).toString();
        String code = verifyCode.getIfPresent(key);
        if (!StringUtils.isBlank(code)) {
            mailCode = code;
        } else {
            mailCode = generateCode();
            verifyCode.put(key, mailCode);
        }

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
    public boolean register(AuthUser user) {
        return false;
    }

    @Override
    public boolean forgot(AuthUser user) {
        return false;
    }

    private String generateCode() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.toString();
    }
}
