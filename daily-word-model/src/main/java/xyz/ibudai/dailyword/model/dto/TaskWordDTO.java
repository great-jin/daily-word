package xyz.ibudai.dailyword.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.dailyword.model.enums.Catalogue;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskWordDTO {

    /**
     * 类型
     */
    private Catalogue catalogue;

    /**
     * 下标
     */
    private Integer offset;

    /**
     * 单词
     */
    private String value;

    /**
     * 单词长度
     */
    private Integer wordLength;

    /**
     * 翻译
     */
    private List<String> translation;

    /**
     * 是否正确
     */
    private Boolean correct;

}
