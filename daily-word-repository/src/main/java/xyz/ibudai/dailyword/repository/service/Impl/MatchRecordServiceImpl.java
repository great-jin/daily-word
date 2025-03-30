package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.config.SystemConfig;
import xyz.ibudai.dailyword.model.dto.RoomDTO;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.model.enums.RankMode;
import xyz.ibudai.dailyword.repository.dao.MatchRecordDao;
import xyz.ibudai.dailyword.repository.service.MatchRecordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * (MatchRecord)表服务实现类
 *
 * @author makejava
 * @since 2025-03-16 09:26:04
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchRecordServiceImpl extends ServiceImpl<MatchRecordDao, MatchRecord> implements MatchRecordService {

    @Override
    public void initRecord(String matchId, Set<Integer> uIdList, RoomDTO roomDTO) {
        if (CollectionUtils.isEmpty(uIdList)) {
            return;
        }

        List<MatchRecord> recordList = new ArrayList<>();
        for (Integer uid : uIdList) {
            Integer roomNumber = roomDTO.getRoomNumber();
            RankMode rankMode = roomDTO.getMode();
            MatchRecord record = MatchRecord.builder()
                    .userId(uid)
                    .season(SystemConfig.getSeason())
                    .groupId(matchId)
                    .rankMode(Objects.isNull(rankMode) ? null : rankMode.name())
                    .roomNumber(Objects.isNull(roomNumber) ? null : String.valueOf(roomNumber))
                    .rankType(roomDTO.getRoomSize())
                    .catalog(roomDTO.getCatalogue().name())
                    .wordCount(roomDTO.getSize())
                    .finished(false)
                    .build();
            recordList.add(record);
        }

        this.saveBatch(recordList);
    }
}

