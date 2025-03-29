package xyz.ibudai.dailyword.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.dto.RoomDTO;
import xyz.ibudai.dailyword.model.entity.MatchRecord;

import java.util.Set;

/**
 * (MatchRecord)表服务接口
 *
 * @author makejava
 * @since 2025-03-16 09:26:04
 */
public interface MatchRecordService extends IService<MatchRecord> {

    /**
     * Init record.
     *
     * @param uIdList the u id list
     * @param roomDTO the room dto
     */
    void initRecord(String matchId, Set<Integer> uIdList, RoomDTO roomDTO);
}

