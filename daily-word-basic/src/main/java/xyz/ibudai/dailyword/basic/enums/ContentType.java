package xyz.ibudai.dailyword.basic.enums;

import lombok.Getter;

/**
 * The enum Content type.
 */
@Getter
public enum ContentType {

    JSON("application/json;charset=UTF-8");


    private final String val;

    ContentType(String val) {
        this.val = val;
    }
}
