package xyz.ibudai.dailyword.socket.enums;

import lombok.Getter;
import xyz.ibudai.dailyword.socket.consts.BeanConst;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum Protocol {

    PREFIX("/dailyWord"),

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

    /**
     * Gets by uri.
     *
     * @param uri the uri
     * @return the by uri
     */
    public static Protocol getByUri(String uri) {
        for (Protocol item : values()) {
            String s = PREFIX.getUri() + item.getUri();
            if (s.equalsIgnoreCase(uri)) {
                return item;
            }
        }
        return DEFAULT;
    }

    /**
     * Gets bean name.
     *
     * @param protocol the protocol
     * @return the bean name
     */
    public static String getBeanName(Protocol protocol) {
        return switch (protocol) {
            case RANK -> BeanConst.Handler.RANK;
            case FRIEND -> BeanConst.Handler.FRIEND;
            default -> BeanConst.Handler.DEFAULT;
        };
    }
}
