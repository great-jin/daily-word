package xyz.ibudai.service;

import xyz.ibudai.model.DictDetail;
import xyz.ibudai.model.TaskWord;
import xyz.ibudai.model.Word;
import xyz.ibudai.model.WordRequest;

import java.util.List;

/**
 * The interface Word service.
 */
public interface WordService {

    /**
     * Gets dict detail.
     *
     * @return the dict detail
     */
    List<DictDetail> getDictDetail();

    /**
     * Translation word.
     *
     * @param target the target
     * @return the word
     */
    Word translation(String target);

    /**
     * Gets task content.
     *
     * @param wordRequest the word request
     * @return the task content
     */
    List<TaskWord> getTaskContent(WordRequest wordRequest);

}
