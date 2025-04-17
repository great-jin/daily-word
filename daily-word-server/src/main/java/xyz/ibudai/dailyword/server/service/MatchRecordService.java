package xyz.ibudai.dailyword.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import xyz.ibudai.dailyword.model.dto.AnswerDTO;
import xyz.ibudai.dailyword.model.dto.RoomDTO;
import xyz.ibudai.dailyword.model.entity.match.MatchRecord;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.match.MatchDetailVo;
import xyz.ibudai.dailyword.model.vo.match.MatchRecordVo;

import java.util.List;
import java.util.Set;

/**
 * (MatchRecord)表服务接口
 *
 * @author budai
 * @since 2025-03-16 09:26:04
 */
public interface MatchRecordService extends IService<MatchRecord> {

    /**
     * Paging page info.
     *
     * @param pageNo   the page no
     * @param pageSize the page size
     * @return the page info
     */
    PageInfo<MatchRecordVo> paging(Integer pageNo, Integer pageSize);

    /**
     * Check task finished boolean.
     *
     * @param matchId the match id
     * @return the boolean
     */
    Boolean checkTaskFinished(Integer matchId);

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
    Integer initRecord(Set<Integer> uIdList, RoomDTO roomDTO);

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

