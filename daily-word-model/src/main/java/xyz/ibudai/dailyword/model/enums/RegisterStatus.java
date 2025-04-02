package xyz.ibudai.dailyword.model.enums;

import lombok.Getter;

/**
 * The enum Register status.
 */
@Getter
public enum RegisterStatus {

    /**
     * Success register status.
     */
    SUCCESS(1),

    /**
     * Failed register status.
     */
    FAILED(2),

    /**
     * Name used register status.
     */
    NAME_USED(3),

    /**
     * Invite code invalid register status.
     */
    INVITE_CODE_INVALID(4),

    /**
     * Email code invalid register status.
     */
    EMAIL_CODE_INVALID(5);


    private final Integer code;

    RegisterStatus(Integer code) {
        this.code = code;
    }
}
