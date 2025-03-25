package xyz.ibudai.dailyword.server.service;

import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.*;
import xyz.ibudai.dailyword.model.vo.word.Word;

import java.util.*;

/**
 * The type Word service.
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
     * @param catalogue the catalogue
     * @param size      the size
     * @return the task content
     */
    List<TaskWord> getTaskContent(Catalogue catalogue, Integer size);
}
