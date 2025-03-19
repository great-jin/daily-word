package xyz.ibudai.dailyword.socket.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum Protocol {

    PREFIX("/dailyword"),

    RANK("/rank"),

    FRIEND("/friend"),

    DEFAULT("/default");

    private final String uri;

    Protocol(String uri) {
        this.uri = uri;
    }


    public static String getSubProtocols() {
        List<String> list = new ArrayList<>();
        for (Protocol value : values()) {
            list.add(value.uri);
        }
        return String.join(",", list);
    }
}
