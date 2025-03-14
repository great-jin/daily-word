package xyz.ibudai.dailyword.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.dailyword.model.enums.Catalogue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordRequest {

    private Catalogue catalogue;

    private Integer size;

    private Integer offset;

}
