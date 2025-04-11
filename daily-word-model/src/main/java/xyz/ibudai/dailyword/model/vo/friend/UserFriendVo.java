package xyz.ibudai.dailyword.model.vo.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendVo {

    private Integer userId;

    private String userName;

    private String avatarUrl;

    private Boolean online;

    private String lastOnline;
}
