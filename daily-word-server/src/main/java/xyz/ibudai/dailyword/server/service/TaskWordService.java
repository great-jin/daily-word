package xyz.ibudai.dailyword.server.service;

import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.*;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.vo.match.MatchVo;
import xyz.ibudai.dailyword.model.vo.word.Word;

import java.util.*;

/**
 * The type Word service.
 */
public interface TaskWordService {

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
     * Star task match vo.
     *
     * @param catalogue the catalogue
     * @param size      the size
     * @return the match vo
     */
    MatchVo starTask(Catalogue catalogue, Integer size);

    /**
     * Gets task content.
     *
     * @param catalogue the catalogue
     * @param size      the size
     * @return the task content
     */
    List<TaskWordDTO> getTaskContent(Catalogue catalogue, Integer size);

    /**
     * Gets answer.
     *
     * @param matchId the match id
     * @return the answer
     */
    AnswerVo getAnswer(Integer matchId);
}
