package xyz.ibudai.dailyword.server.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.dto.AnswerDTO;
import xyz.ibudai.dailyword.model.entity.match.MatchRecord;
import xyz.ibudai.dailyword.server.service.MatchRecordService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchSchedule {

    private final MatchRecordService matchRecordService;


    /**
     * 每二小时执行一次，手动提交二小时内未完结任务
     */
    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.HOURS)
    public void manualSubmitMatch() {
        LocalDateTime twoHourEarly = LocalDateTime.now().minusHours(2L);
        List<MatchRecord> undoneList = matchRecordService.lambdaQuery()
                .le(MatchRecord::getCreateTime, twoHourEarly)
                .eq(MatchRecord::getFinished, Boolean.FALSE)
                .list();
        if (CollectionUtils.isEmpty(undoneList)) {
            return;
        }

        // 按对局分组
        Map<Integer, List<MatchRecord>> groupMap = undoneList
                .stream().collect(Collectors.groupingBy(MatchRecord::getMatchId));
        for (Map.Entry<Integer, List<MatchRecord>> item : groupMap.entrySet()) {
            try {
                AnswerDTO answerDTO = new AnswerDTO();
                answerDTO.setMatchId(item.getKey());
                answerDTO.setCostTime(0);
                answerDTO.setScore(0);
                answerDTO.setContentList(new ArrayList<>());
                matchRecordService.finishMatch(answerDTO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
