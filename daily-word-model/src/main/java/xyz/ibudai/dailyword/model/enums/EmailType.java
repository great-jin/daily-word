package xyz.ibudai.dailyword.model.enums;

import lombok.Getter;

@Getter
public enum EmailType {

    Register(1),

    Forgot(2);


    private final Integer code;

    EmailType(Integer code) {
        this.code = code;
    }
}
