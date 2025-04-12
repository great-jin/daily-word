package xyz.ibudai.dailyword.model.vo.friend;

import lombok.Data;

@Data
public class FriendInviteVo {

    private Integer userId;

    private String username;

    private String avatarUrl;

    private Integer processStatus;
}
