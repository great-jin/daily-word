package xyz.ibudai.dailyword.model.vo.friend;

import lombok.Data;

@Data
public class FriendInviteVo {

    private Integer userId;

    private String username;

    private String avatarUrl;

    /**
     * 申请时间间隔
     */
    private String dateInterval;

    private Integer processStatus;

}
