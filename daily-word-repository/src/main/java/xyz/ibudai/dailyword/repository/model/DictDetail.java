package xyz.ibudai.dailyword.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.dailyword.repository.enums.Catalogue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictDetail {

    private Catalogue catalogue;

    private Integer wordCount;

}
