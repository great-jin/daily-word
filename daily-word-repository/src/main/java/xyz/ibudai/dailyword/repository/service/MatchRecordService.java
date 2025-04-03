package xyz.ibudai.dailyword.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.dto.AnswerDTO;
import xyz.ibudai.dailyword.model.dto.RoomDTO;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.match.MatchDetailVo;

import java.util.List;
import java.util.Set;

/**
 * (MatchRecord)表服务接口
 *
 * @author makejava
 * @since 2025-03-16 09:26:04
 */
public interface MatchRecordService extends IService<MatchRecord> {

    /**
     * Check available boolean.
     *
     * @param catalogue the catalogue
     * @return the boolean
     */
    Boolean checkAvailable(Catalogue catalogue);

    /**
     * Init record.
     *
     * @param uIdList the u id list
     * @param roomDTO the room dto
     */
    void initRecord(String matchId, Set<Integer> uIdList, RoomDTO roomDTO);

    /**
     * Finish match.
     *
     * @param answerDTO the answer dto
     */
    void finishMatch(AnswerDTO answerDTO);

    /**
     * All done handler.
     *
     * @param answerDTO    the answer dto
     * @param userRecord   the user record
     * @param otherRecords the other records
     */
    void allDoneHandler(AnswerDTO answerDTO, MatchRecord userRecord, List<MatchRecord> otherRecords);

    /**
     * Gets details.
     *
     * @param matchId the match id
     * @return the details
     */
    List<MatchDetailVo> getDetails(String matchId);
}

