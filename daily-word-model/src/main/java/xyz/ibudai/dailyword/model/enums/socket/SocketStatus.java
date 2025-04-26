package xyz.ibudai.dailyword.model.enums.socket;

import lombok.Getter;

/**
 * The enum Socket status.
 */
@Getter
public enum SocketStatus {

    /**
     * Match repeat.
     */
    MATCH_REPEAT(300),

    /**
     * In matching.
     */
    RANK_MATCHING(301),

    /**
     * Match success.
     */
    RANK_MATCHED(302),

    /**
     * Match fail.
     */
    RANK_NOT_MATCHED(303);


    private final Integer code;

    SocketStatus(Integer code) {
        this.code = code;
    }
}
