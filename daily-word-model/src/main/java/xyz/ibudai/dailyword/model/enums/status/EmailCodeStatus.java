package xyz.ibudai.dailyword.model.enums.status;

import lombok.Getter;

/**
 * The enum Email code status.
 */
@Getter
public enum EmailCodeStatus {

    SUCCESS(1),

    FAIL(2),

    EMAIL_INVALID(3),

    EMAIL_IN_USE(4);


    private final Integer status;

    EmailCodeStatus(Integer status) {
        this.status = status;
    }
}
