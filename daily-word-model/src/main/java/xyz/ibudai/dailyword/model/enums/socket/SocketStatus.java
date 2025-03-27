package xyz.ibudai.dailyword.model.enums.socket;

import lombok.Getter;

/**
 * The enum Socket status.
 */
@Getter
public enum SocketStatus {

    /**
     * Rank matching socket status.
     */
    RANK_MATCHING(301),

    /**
     * Rank matched socket status.
     */
    RANK_MATCHED(302),

    /**
     * Rank not matched socket status.
     */
    RANK_NOT_MATCHED(303);


    private final Integer code;

    SocketStatus(Integer code) {
        this.code = code;
    }
}
