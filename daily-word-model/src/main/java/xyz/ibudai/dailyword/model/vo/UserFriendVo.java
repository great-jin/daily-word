package xyz.ibudai.dailyword.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendVo {

    private Integer userId;

    private String userName;

    private Boolean online;
}
