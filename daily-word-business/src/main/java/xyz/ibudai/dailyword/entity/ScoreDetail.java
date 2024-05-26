package xyz.ibudai.dailyword.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (ScoreDetail)表实体类
 *
 * @author makejava
 * @since 2023-12-16 09:59:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDetail extends Model<ScoreDetail> {

    private String id;

    private String userName;

}

