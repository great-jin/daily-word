package xyz.ibudai.dailyword.server.resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.config.SystemConfig;
import xyz.ibudai.dailyword.model.dto.AnswerDTO;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.match.MatchDetailVo;
import xyz.ibudai.dailyword.repository.service.MatchRecordService;
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
    public PageInfo<MatchRecord> listHistory(@RequestParam("pageNo") Integer pageNo,
                                             @RequestParam("pageSize") Integer pageSize) {
        Integer loginUser = SecurityUtil.getLoginUser();
        if (Objects.isNull(loginUser)) {
            return new PageInfo<>(Collections.emptyList());
        }

        PageHelper.startPage(pageNo, pageSize);
        List<MatchRecord> list = matchRecordService.lambdaQuery()
                .eq(MatchRecord::getUserId, SecurityUtil.getLoginUser())
                .eq(MatchRecord::getSeason, SystemConfig.getSeason())
                .orderByDesc(MatchRecord::getCreateTime)
                .list();
        return new PageInfo<>(list);
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
