package xyz.ibudai.dailyword.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.ibudai.dailyword.model.entity.RankBoard;
import xyz.ibudai.dailyword.model.vo.RankBoardVo;

import java.util.List;

/**
 * (RankBoard)表数据库访问层
 *
 * @author budai
 * @since 2025-03-16 09:26:04
 */
public interface RankBoardDao extends BaseMapper<RankBoard> {

    /**
     * List top fifty.
     *
     * @param catalog the catalog
     * @param season  the season
     * @return the list
     */
    List<RankBoardVo> listTopFifty(String catalog, Integer season);
}

