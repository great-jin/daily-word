package xyz.ibudai.dailyword.basic.enums;

import lombok.Getter;

/**
 * The enum Http header.
 */
@Getter
public enum HttpHeader {

    /**
     * Token http header.
     */
    TOKEN("Token", "token"),

    /**
     * Authentic http header.
     */
    AUTHENTIC("auth", "auth");


    private final String frontend;

    private final String backend;

    HttpHeader(String frontend, String backend) {
        this.frontend = frontend;
        this.backend = backend;
    }
}
