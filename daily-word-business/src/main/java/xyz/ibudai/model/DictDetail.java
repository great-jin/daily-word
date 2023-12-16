package xyz.ibudai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.model.common.Catalogue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictDetail {

    private Catalogue catalogue;

    private Integer wordCount;

}
