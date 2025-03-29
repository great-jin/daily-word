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

    private Catalogue catalogue;

    private Integer offset;

    private String value;

    private List<String> translation;

}
