package xyz.ibudai.dailyword.server.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.dto.AnswerDTO;
import xyz.ibudai.dailyword.model.entity.match.MatchRecord;
import xyz.ibudai.dailyword.model.entity.user.AuthUser;
import xyz.ibudai.dailyword.server.service.MatchRecordService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchSchedule {

    private final MatchRecordService matchRecordService;


    /**
     * 半小时执行一次，手动提交二小时内未完结任务
     */
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void manualSubmitMatch() {
        LocalDateTime twoHourEarly = LocalDateTime.now().minusHours(2L);
        List<MatchRecord> undoneList = matchRecordService.lambdaQuery()
                // 超过两小时且未完结
                .le(MatchRecord::getCreateTime, twoHourEarly)
                .eq(MatchRecord::getFinished, Boolean.FALSE)
                .list();
        if (CollectionUtils.isEmpty(undoneList)) {
            return;
        }

        for (MatchRecord matchRecord : undoneList) {
            Integer userId = null;
            Integer matchId = null;
            try {
                userId = matchRecord.getUserId();
                matchId = matchRecord.getMatchId();

                // 设置用户上下文信息
                AuthUser authUser = new AuthUser();
                authUser.setId(userId);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(authUser, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 提交对局
                AnswerDTO answerDTO = new AnswerDTO();
                answerDTO.setMatchId(matchRecord.getMatchId());
                answerDTO.setCostTime(0);
                answerDTO.setScore(0);
                answerDTO.setContentList(new ArrayList<>());
                matchRecordService.finishMatch(answerDTO);
            } catch (Exception e) {
                log.error("Manual submit task fail, userId: {}, matchId: {}", userId, matchId, e);
            } finally {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        }
    }
}
