package xyz.ibudai.dailyword.model.enums;

import lombok.Getter;

@Getter
public enum EmailType {

    Register(1, "注册账号"),

    Forgot(2, "修改密码");


    private final Integer code;

    private final String mode;

    EmailType(Integer code, String mode) {
        this.code = code;
        this.mode = mode;
    }

    public static EmailType getByType(Integer code) {
        for (EmailType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
