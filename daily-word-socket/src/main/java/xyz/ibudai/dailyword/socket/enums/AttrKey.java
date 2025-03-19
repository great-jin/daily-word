package xyz.ibudai.dailyword.socket.enums;

import lombok.Getter;

@Getter
public enum AttrKey {

    UID("userId"),

    AUTHED("authed");

    private final String key;

    AttrKey(String key) {
        this.key = key;
    }
}
