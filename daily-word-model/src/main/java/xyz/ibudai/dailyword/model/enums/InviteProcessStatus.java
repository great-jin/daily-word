package xyz.ibudai.dailyword.model.enums;

import lombok.Getter;

@Getter
public enum InviteProcessStatus {

    Pending(0),

    Ok(1),

    Reject(2);


    private final Integer status;

    InviteProcessStatus(Integer status) {
        this.status = status;
    }
}
