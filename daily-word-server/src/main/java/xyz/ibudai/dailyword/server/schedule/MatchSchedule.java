package xyz.ibudai.dailyword.server.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.repository.service.MatchRecordService;

import java.time.LocalDateTime;
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
        Map<String, List<MatchRecord>> groupMap = undoneList
                .stream().collect(Collectors.groupingBy(MatchRecord::getGroupId));

        // TODO 2025/4/3 超时用户默认判负
        // TODO 2025/4/3 人机挑战直接结束


    }
}
