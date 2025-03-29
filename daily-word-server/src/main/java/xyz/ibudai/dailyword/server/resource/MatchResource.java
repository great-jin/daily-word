package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.dailyword.model.config.SystemConfig;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.repository.service.MatchRecordService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/server/match")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchResource {

    private final MatchRecordService matchRecordService;


    @PostMapping("submit")
    public void submit() {
        // TODO 2025/3/27 提交对局

        // 根据对局 Key 查询库是否对手完成
        // 无匹配数据保存自身对局并生成记录


    }

    /**
     * List history list.
     *
     * @return the list
     */
    @GetMapping("listHistory")
    public List<MatchRecord> listHistory() {
        Integer loginUser = SecurityUtil.getLoginUser();
        if (Objects.isNull(loginUser)) {
            return new ArrayList<>();
        }

        return matchRecordService.lambdaQuery()
                .eq(MatchRecord::getId, SecurityUtil.getLoginUser())
                .eq(MatchRecord::getSeason, SystemConfig.getSeason())
                .list();
    }
}
