package xyz.ibudai.dailyword.model.enums.status;

import lombok.Getter;

@Getter
public enum PasswordStatus {

    SUCCESS("成功"),

    FAILED("失败"),

    CAPTCHA_MISMATCH("验证码无效"),

    PWD_MISMATCH("密码错误"),

    PWD_SAME("新旧密码不可相同"),

    EMAIL_NOT_REGISTER("邮箱尚未注册");


    private final String message;

    PasswordStatus(String message) {
        this.message = message;
    }
}
