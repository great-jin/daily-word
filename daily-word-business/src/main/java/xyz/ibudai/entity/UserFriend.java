package xyz.ibudai.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (UserFriend)表实体类
 *
 * @author makejava
 * @since 2023-12-16 09:59:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriend extends Model<UserFriend> {

    private String userName;

    private String friendName;

    private Date createTime;

    private String deleted;

}

