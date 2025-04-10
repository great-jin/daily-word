package xyz.ibudai.dailyword.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.config.SystemConfig;
import xyz.ibudai.dailyword.model.dto.AnswerDTO;
import xyz.ibudai.dailyword.model.dto.RoomDTO;
import xyz.ibudai.dailyword.model.entity.MatchDetail;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.enums.RankMode;
import xyz.ibudai.dailyword.model.enums.RankType;
import xyz.ibudai.dailyword.model.vo.match.MatchDetailVo;
import xyz.ibudai.dailyword.model.vo.match.MatchRecordVo;
import xyz.ibudai.dailyword.repository.dao.MatchRecordDao;
import xyz.ibudai.dailyword.server.service.MatchDetailService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;
import xyz.ibudai.dailyword.server.service.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (MatchRecord)表服务实现类
 *
 * @author budai
 * @since 2025-03-16 09:26:04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchRecordServiceImpl extends ServiceImpl<MatchRecordDao, MatchRecord> implements MatchRecordService {

    private final MongoService mongoService;

    private final AuthUserService authUserService;

    private final RankBoardService rankBoardService;
    private final MatchDetailService matchDetailService;


    @Override
    public PageInfo<MatchRecordVo> paging(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);

        List<MatchRecordVo> dataList = this.baseMapper.listByUser(
                SecurityUtil.getLoginUser(),
                SystemConfig.getSeason()
        );
        return new PageInfo<>(dataList);
    }

    @Override
    public Boolean checkAvailable(Catalogue catalogue) {
        if (LocalDateTime.now().isAfter(SystemConfig.getMonthLastHour())) {
            // 当月最后一小时不允许新对局
            return false;
        }

        // 是否有进行中任务
        List<MatchRecord> undoneList = this.lambdaQuery()
                .eq(MatchRecord::getUserId, SecurityUtil.getLoginUser())
                // TODO 过滤 catalog
                .eq(MatchRecord::getSeason, SystemConfig.getSeason())
                .eq(MatchRecord::getFinished, Boolean.FALSE)
                .list();

        // 超过指定数量不允许开始新对局
        return CollectionUtils.isEmpty(undoneList)
                || undoneList.size() < SystemConfig.getUndoneLimit();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer initRecord(Set<Integer> uIdList, RoomDTO roomDTO) {
        if (CollectionUtils.isEmpty(uIdList)) {
            return 0;
        }

        // 房间信息
        Integer roomNumber = roomDTO.getRoomNumber();
        RankMode rankMode = roomDTO.getMode();
        MatchDetail matchDetail = MatchDetail.builder()
                .rankMode(Objects.isNull(rankMode) ? null : rankMode.name())
                .roomNumber(Objects.isNull(roomNumber) ? null : String.valueOf(roomNumber))
                .rankType(roomDTO.getRoomSize())
                .catalog(roomDTO.getCatalogue().name())
                .wordCount(roomDTO.getSize())
                .wordIndies(roomDTO.getWordIndies())
                .build();
        matchDetailService.save(matchDetail);

        // 用户记录
        Integer matchId = matchDetail.getId();
        List<MatchRecord> recordList = new ArrayList<>();
        for (Integer uid : uIdList) {
            MatchRecord record = MatchRecord.builder()
                    .userId(uid)
                    .season(SystemConfig.getSeason())
                    .matchId(matchId)
                    .finished(false)
                    .build();
            recordList.add(record);
        }
        MatchRecordService proxy = (MatchRecordService) AopContext.currentProxy();
        proxy.saveBatch(recordList);

        return matchId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishMatch(AnswerDTO answerDTO) {
        // 匹配对局信息
        Integer matchId = answerDTO.getMatchId();
        MatchDetail matchDetail = matchDetailService.getById(matchId);
        if (Objects.isNull(matchDetail)) {
            log.info("The match of {} not found", matchId);
            return;
        }

        LocalDateTime nowadays = LocalDateTime.now();
        boolean allDone = true;
        MatchRecord userRecord = null;
        List<MatchRecord> otherRecords = new ArrayList<>();
        // 状态分组
        List<MatchRecord> recordList = this.lambdaQuery()
                .eq(MatchRecord::getMatchId, matchId)
                .list();
        for (MatchRecord item : recordList) {
            item.setUpdateTime(nowadays);
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

        // 计算答题数据，保存 mongo
        int correctCount = mongoService.getCorrectCount(matchId, answerDTO.getContentList());
        userRecord.setFinished(Boolean.TRUE);
        userRecord.setCorrectCount(correctCount);
        userRecord.setCostSecond(answerDTO.getCostTime());

        // 创建代理对象
        MatchRecordService proxy = (MatchRecordService) AopContext.currentProxy();
        if (Objects.equals(matchDetail.getRankType(), RankType.MACHINE.getCount())) {
            // 单人挑战，只更新自身后便结束
            userRecord.setScore(0);
            proxy.updateById(userRecord);
            return;
        }

        if (!allDone) {
            // 其它人未完成则只更新自身答题结果
            proxy.updateById(userRecord);
            return;
        }
        // 所有人都结束
        proxy.allDoneHandler(answerDTO, userRecord, otherRecords);
    }

    /**
     * All done handler.
     *
     * @param answerDTO    the answer dto
     * @param userRecord   the user record
     * @param otherRecords the other records
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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

        // 更新匹配记录
        MatchRecordService proxy = (MatchRecordService) AopContext.currentProxy();
        proxy.updateBatchById(otherRecords);

        // 更新排行榜
        rankBoardService.updateRankBoard(otherRecords);
    }

    @Override
    public List<MatchDetailVo> getDetails(String matchId) {
        List<MatchRecord> recordList = this.lambdaQuery()
                .eq(MatchRecord::getMatchId, matchId)
                .list();
        if (CollectionUtils.isEmpty(recordList)) {
            return Collections.emptyList();
        }

        Set<Integer> userIds = recordList.stream()
                .map(MatchRecord::getUserId)
                .collect(Collectors.toSet());
        Map<Integer, String> userMap = authUserService.groupByIds(userIds);
        // 构建前端对象
        List<MatchDetailVo> resultList = new ArrayList<>();
        for (MatchRecord item : recordList) {
            MatchDetailVo detailVo = MatchDetailVo.builder()
                    .userName(userMap.get(item.getUserId()))
                    .costSecond(item.getCostSecond())
                    .correctCount(item.getCorrectCount())
                    .score(item.getScore())
                    .build();
            resultList.add(detailVo);
        }
        return resultList;
    }
}

