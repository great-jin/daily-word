package xyz.ibudai.dailyword.model.enums.status;

import lombok.Getter;

/**
 * The enum Register status.
 */
@Getter
public enum RegisterStatus {

    SUCCESS(1, "成功"),

    FAILED(2, "注册失败"),

    USERNAME_INVALID(3, "用户名格式非法"),

    NAME_USED(4, "用户名已被注册"),

    INVITE_CODE_INVALID(5, "邀请码无效"),

    EMAIL_CODE_INVALID(6, "验证码无效"),;


    private final Integer code;

    private final String message;

    RegisterStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
