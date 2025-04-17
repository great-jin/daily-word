package xyz.ibudai.dailyword.basic.enums;

import lombok.Getter;

/**
 * The enum Login status.
 */
@Getter
public enum LoginStatus {

    NOT_EXIST("用户名不存在"),

    NOT_LOGIN("请登录后操作."),

    LOCKED("账号已锁定"),

    BAD_CREDENTIAL("密码错误，请检查后重试"),

    EXPIRE("登录过期"),

    NOT_AUTH("无访问权限，请登录后操作");


    private final String msg;

    LoginStatus(String msg) {
        this.msg = msg;
    }
}
