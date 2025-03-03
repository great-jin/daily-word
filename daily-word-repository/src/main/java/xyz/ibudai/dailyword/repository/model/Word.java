package xyz.ibudai.dailyword.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.dailyword.repository.enums.Catalogue;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {

    private Boolean existed;

    private String value;

    private Catalogue catalogue;

    private List<WordDescribe> describeList;


    public static Word notFound(String value) {
        Word word = new Word();
        word.setExisted(false);
        word.setValue(value);
        word.setDescribeList(Collections.emptyList());
        return word;
    }
}
