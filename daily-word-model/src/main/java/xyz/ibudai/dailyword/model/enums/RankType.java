package xyz.ibudai.dailyword.model.enums;

import lombok.Getter;

/**
 * The enum Rank type.
 */
@Getter
public enum RankType {

    MACHINE(0),

    TWO_PLAYER(2),

    THREE_PLAYER(3),

    FIVE_PLAYER(5);


    private final Integer count;

    RankType(Integer count) {
        this.count = count;
    }
}
