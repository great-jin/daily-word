package xyz.ibudai.dailyword.model.enums.status;

import lombok.Getter;

@Getter
public enum PasswordStatus {

    SUCCESS(1),

    FAILED(2),

    CAPTCHA_MISMATCH(3),

    PWD_MISMATCH(4),

    PWD_SAME(5),

    EMAIL_NOT_REGISTER(6);


    private final Integer code;

    PasswordStatus(Integer code) {
        this.code = code;
    }
}
