package xyz.ibudai.dailyword.model.enums.status;

import lombok.Getter;

/**
 * The enum Email code status.
 */
@Getter
public enum EmailCodeStatus {

    SUCCESS("成功"),

    FAIL("发送失败"),

    EMAIL_INVALID("邮箱格式非法"),

    EMAIL_IN_USE("邮箱已被注册"),

    EMAIL_NOT_REGISTER("邮箱未注册"),

    REQ_TO_MANY("发送过于频率，请稍后重试");


    private final String message;

    EmailCodeStatus(String message) {
        this.message = message;
    }
}
