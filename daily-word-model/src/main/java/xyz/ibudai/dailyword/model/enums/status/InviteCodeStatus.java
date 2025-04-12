package xyz.ibudai.dailyword.model.enums.status;

import lombok.Getter;

/**
 * The enum Invite code status.
 */
@Getter
public enum InviteCodeStatus {

    UN_USE(1),

    USED(2),

    EXPIRE(3);


    private final Integer status;

    InviteCodeStatus(Integer status) {
        this.status = status;
    }
}
