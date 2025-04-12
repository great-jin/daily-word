package xyz.ibudai.dailyword.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.ibudai.dailyword.model.entity.match.MatchRecord;
import xyz.ibudai.dailyword.model.vo.match.MatchRecordVo;

import java.util.List;

/**
 * (MatchRecord)表数据库访问层
 *
 * @author budai
 * @since 2025-03-16 09:26:04
 */
public interface MatchRecordDao extends BaseMapper<MatchRecord> {

    /**
     * Paging list.
     *
     * @param userId the user id
     * @param season the season
     * @return the list
     */
    List<MatchRecordVo> listByUser(Integer userId, Integer season);
}

