package xyz.ibudai.dailyword.model.enums.status;

import lombok.Getter;

/**
 * The enum Register status.
 */
@Getter
public enum RegisterStatus {

    SUCCESS(1),

    FAILED(2),

    NAME_USED(3),

    INVITE_CODE_INVALID(4),

    EMAIL_CODE_INVALID(5),

    INVITE_USERNAME(6),

    CAPTCHA_MISMATCH(7);


    private final Integer code;

    RegisterStatus(Integer code) {
        this.code = code;
    }
}
