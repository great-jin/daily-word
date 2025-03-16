package xyz.ibudai.dailyword.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (DictCatalog)表实体类
 *
 * @author ibudai
 * @since 2025-03-16 09:26:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dict_catalog")
public class DictCatalog extends Model<DictCatalog> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("catalog")
    private String catalog;

    @TableField("catalog_name")
    private String catalogName;

}

