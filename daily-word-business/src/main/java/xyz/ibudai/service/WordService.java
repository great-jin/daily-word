package xyz.ibudai.service;

import xyz.ibudai.model.TaskWord;
import xyz.ibudai.model.Word;
import xyz.ibudai.model.WordRequest;

import java.util.List;

public interface WordService {

    Word translation(String target);

    List<TaskWord> getTaskContent(WordRequest wordRequest);

}
