package xyz.ibudai.dailyword.model.enums.status;

import lombok.Getter;

/**
 * The enum Email code status.
 */
@Getter
public enum EmailCodeStatus {

    SUCCESS(1, "成功"),

    FAIL(2, "发送失败"),

    EMAIL_INVALID(3, "邮箱格式非法"),

    EMAIL_IN_USE(4, "邮箱已被注册");


    private final Integer status;

    private final String message;

    EmailCodeStatus(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
