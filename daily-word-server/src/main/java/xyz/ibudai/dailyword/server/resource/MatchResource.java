package xyz.ibudai.dailyword.server.resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.config.SystemConfig;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.repository.service.MatchRecordService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.util.*;

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
    public PageInfo<MatchRecord> listHistory(@RequestParam("pageNo") Integer pageNo,
                                             @RequestParam("pageSize") Integer pageSize) {
        Integer loginUser = SecurityUtil.getLoginUser();
        if (Objects.isNull(loginUser)) {
            return new PageInfo<>(Collections.emptyList());
        }

        PageHelper.startPage(pageNo, pageSize);
        List<MatchRecord> list = matchRecordService.lambdaQuery()
                .eq(MatchRecord::getId, SecurityUtil.getLoginUser())
                .eq(MatchRecord::getSeason, SystemConfig.getSeason())
                .list();
        return new PageInfo<>(list);
    }
}
