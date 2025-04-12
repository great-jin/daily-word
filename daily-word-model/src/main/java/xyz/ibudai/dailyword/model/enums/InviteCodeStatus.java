package xyz.ibudai.dailyword.model.enums;

import lombok.Getter;

/**
 * The enum Invite code status.
 */
@Getter
public enum InviteCodeStatus {

    /**
     * Un use invite code status.
     */
    UN_USE(1),

    /**
     * Used invite code status.
     */
    USED(2),

    /**
     * Expire invite code status.
     */
    EXPIRE(3);


    private final Integer status;

    InviteCodeStatus(Integer status) {
        this.status = status;
    }
}
