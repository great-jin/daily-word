package xyz.ibudai.dailyword.basic.enums;

import lombok.Getter;

/**
 * The enum Login status.
 */
@Getter
public enum LoginStatus {

    NOT_EXIST("Account doesn't exist, please recheck."),

    NOT_LOGIN("Please login first."),

    LOCKED("Account has been locked, please contact the administrator."),

    BAD_CREDENTIAL("Account credential error, please recheck."),

    EXPIRE("Login expired."),

    NOT_AUTH("Authorization failure, please login and try again.");


    private final String msg;

    LoginStatus(String msg) {
        this.msg = msg;
    }
}
