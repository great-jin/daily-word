package xyz.ibudai.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (WordPlan)表实体类
 *
 * @author makejava
 * @since 2023-12-16 09:59:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordPlan extends Model<WordPlan> {

    private String id;

    private String userName;

    private String catalogue;

    private Integer totalSize;

    private Integer batchSize;

    private Integer offset;

    private Integer finished;

}

