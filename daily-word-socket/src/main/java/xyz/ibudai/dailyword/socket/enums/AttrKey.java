package xyz.ibudai.dailyword.socket.enums;

import lombok.Getter;

@Getter
public enum AttrKey {

    UID("key"),

    AUTHED("authed");

    private final String key;

    AttrKey(String key) {
        this.key = key;
    }
}
