package xyz.ibudai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.model.common.Catalogue;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {

    private boolean existed;

    private String value;

    List<WordDescribe> describeList;

    private Catalogue catalogue;
}
