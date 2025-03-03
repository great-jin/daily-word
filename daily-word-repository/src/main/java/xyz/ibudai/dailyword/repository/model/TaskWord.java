package xyz.ibudai.dailyword.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.dailyword.repository.enums.Catalogue;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskWord {

    private Integer offset;

    private String value;

    private List<String> translation;

    private Catalogue catalogue;

}
