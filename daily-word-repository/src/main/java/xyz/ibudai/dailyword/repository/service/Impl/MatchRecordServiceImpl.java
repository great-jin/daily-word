package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.config.SystemConfig;
import xyz.ibudai.dailyword.model.dto.AnswerDTO;
import xyz.ibudai.dailyword.model.dto.RoomDTO;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.enums.RankMode;
import xyz.ibudai.dailyword.repository.dao.MatchRecordDao;
import xyz.ibudai.dailyword.repository.service.MatchRecordService;
import xyz.ibudai.dailyword.repository.service.RankBoardService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.*;

/**
 * (MatchRecord)表服务实现类
 *
 * @author makejava
 * @since 2025-03-16 09:26:04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchRecordServiceImpl extends ServiceImpl<MatchRecordDao, MatchRecord> implements MatchRecordService {

    private final RankBoardService rankBoardService;


    @Override
    public Boolean checkAvailable(Catalogue catalogue) {
        if (LocalDateTime.now().isAfter(SystemConfig.getMonthLastHour())) {
            // 当月最后一小时不允许新对局
            return false;
        }

        // 是否有进行中任务
        List<MatchRecord> undoneList = this.lambdaQuery()
                .eq(MatchRecord::getUserId, SecurityUtil.getLoginUser())
                .eq(MatchRecord::getCatalog, catalogue)
                .eq(MatchRecord::getSeason, SystemConfig.getSeason())
                .eq(MatchRecord::getFinished, Boolean.FALSE)
                .list();

        // 超过指定数量不允许开始新对局
        return CollectionUtils.isEmpty(undoneList)
                || undoneList.size() < SystemConfig.getUndoneLimit();
    }

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

    @Override
    public void finishMatch(AnswerDTO answerDTO) {
        boolean allDone = true;
        MatchRecord userRecord = null;
        List<MatchRecord> otherRecords = new ArrayList<>();
        // 状态分组
        List<MatchRecord> recordList = this.lambdaQuery()
                .eq(MatchRecord::getGroupId, answerDTO.getMatchId())
                .list();
        for (MatchRecord item : recordList) {
            if (Objects.equals(item.getUserId(), SecurityUtil.getLoginUser())) {
                userRecord = item;
                continue;
            }

            if (!item.getFinished()) {
                // 对局其它人员未完成
                allDone = false;
            }
            otherRecords.add(item);
        }
        if (Objects.isNull(userRecord)) {
            log.error("User {} initial record not found, matchId: {}",
                    SecurityUtil.getLoginUser(), answerDTO.getMatchId());
            return;
        }

        userRecord.setFinished(Boolean.TRUE);
        userRecord.setCorrectCount(answerDTO.getCorrectCount());
        userRecord.setCostSecond(answerDTO.getCostTime());
        if (otherRecords.isEmpty()) {
            // 单人挑战
            userRecord.setScore(0);
            this.updateById(userRecord);
            return;
        }
        if (!allDone) {
            // 未完成则只更新自身答题结果
            this.updateById(userRecord);
            return;
        }
        // 所有人都结束
        this.allDoneHandler(answerDTO, userRecord, otherRecords);
    }


    /**
     * All done handler.
     *
     * @param answerDTO    the answer dto
     * @param userRecord   the user record
     * @param otherRecords the other records
     */
    public void allDoneHandler(AnswerDTO answerDTO, MatchRecord userRecord, List<MatchRecord> otherRecords) {
        otherRecords.add(userRecord);
        MatchRecord winner = otherRecords.stream()
                // 根据答对数量降序
                .max(Comparator.comparingInt(MatchRecord::getCorrectCount)
                        // 数量一致取时间最小值
                        .thenComparingInt(MatchRecord::getCostSecond))
                .orElse(null);
        Integer score = answerDTO.getScore();
        for (MatchRecord item : otherRecords) {
            if (Objects.equals(item.getUserId(), winner.getUserId())) {
                item.setScore(score);
                continue;
            }

            // 输则对应积分为负
            item.setScore(score * -1);
        }
        this.updateBatchById(otherRecords);

        // 非单人匹配，更新用户排行榜
        if (!Objects.equals(userRecord.getRankType(), 0)) {
            rankBoardService.updateRankBoard(otherRecords);
        }
    }
}

