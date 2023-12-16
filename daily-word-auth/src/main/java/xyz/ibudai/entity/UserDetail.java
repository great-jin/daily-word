package xyz.ibudai.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserDetail)表实体类
 *
 * @author makejava
 * @since 2023-12-16 10:30:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail extends Model<UserDetail> {

    private String userName;

    private String email;

    private Integer score;

    private Integer locked;

    private Integer enabled;
}

