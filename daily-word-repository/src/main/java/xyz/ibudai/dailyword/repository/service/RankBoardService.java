package xyz.ibudai.dailyword.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.model.entity.RankBoard;
import xyz.ibudai.dailyword.model.vo.RankBoardVo;

import java.util.List;

/**
 * (RankBoard)表服务接口
 *
 * @author budai
 * @since 2025 -03-16 09:26:04
 */
public interface RankBoardService extends IService<RankBoard> {

    /**
     * Gets user rank.
     *
     * @return the user rank
     */
    RankBoard getUserRank(Integer userId);

    /**
     * List by catalog list.
     *
     * @param catalog the catalog
     * @return the list
     */
    List<RankBoardVo> listByCatalog(String catalog);

    /**
     * Update rank board.
     *
     * @param recordList the record list
     */
    void updateRankBoard(List<MatchRecord> recordList);
}

