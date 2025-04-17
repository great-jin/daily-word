package xyz.ibudai.dailyword.model.enums.status;

import lombok.Getter;

@Getter
public enum PasswordStatus {

    SUCCESS(1, "成功"),

    FAILED(2, "失败"),

    CAPTCHA_MISMATCH(3, "验证码无效"),

    PWD_MISMATCH(4, "密码错误"),

    PWD_SAME(5, "新旧密码不可相同"),

    EMAIL_NOT_REGISTER(6, "邮箱尚未注册");


    private final Integer code;

    private final String message;

    PasswordStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
