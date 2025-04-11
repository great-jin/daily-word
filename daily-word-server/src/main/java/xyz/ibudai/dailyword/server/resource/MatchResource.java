package xyz.ibudai.dailyword.server.resource;

import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.dto.AnswerDTO;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.match.MatchDetailVo;
import xyz.ibudai.dailyword.model.vo.match.MatchRecordVo;
import xyz.ibudai.dailyword.server.service.MatchRecordService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.util.*;

@RestController
@RequestMapping("/api/server/match")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchResource {

    private final MatchRecordService matchRecordService;


    /**
     * Check has undone task
     *
     * @param catalogue the catalogue
     * @return the boolean
     */
    @GetMapping("validate")
    public Boolean checkAvailable(@RequestParam("catalog") Catalogue catalogue) {
        return matchRecordService.checkAvailable(catalogue);
    }

    /**
     * Check status boolean.
     *
     * @param matchId the match id
     * @return the boolean
     */
    @GetMapping("checkStatus")
    public Boolean checkStatus(@RequestParam("matchId") Integer matchId) {
        MatchRecord matchRecord = matchRecordService.lambdaQuery()
                .select(MatchRecord::getFinished)
                .eq(MatchRecord::getMatchId, matchId)
                .one();
        return Boolean.TRUE.equals(matchRecord.getFinished());
    }

    /**
     * Submit.
     *
     * @param answerDTO the answer dto
     */
    @PostMapping("submit")
    public void submit(@RequestBody AnswerDTO answerDTO) {
        matchRecordService.finishMatch(answerDTO);
    }

    /**
     * List history list.
     *
     * @return the list
     */
    @GetMapping("listHistory")
    public PageInfo<MatchRecordVo> listHistory(@RequestParam("pageNo") Integer pageNo,
                                               @RequestParam("pageSize") Integer pageSize) {
        Integer loginUser = SecurityUtil.getLoginUser();
        if (Objects.isNull(loginUser)) {
            return new PageInfo<>(Collections.emptyList());
        }

        return matchRecordService.paging(pageNo, pageSize);
    }

    /**
     * Gets detail.
     *
     * @param matchId the match id
     * @return the detail
     */
    @GetMapping("getDetail")
    public List<MatchDetailVo> getDetail(@RequestParam("matchId") String matchId) {
        return matchRecordService.getDetails(matchId);
    }
}
