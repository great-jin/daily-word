package xyz.ibudai.dailyword.server.resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server/match")
public class MatchResource {

    @PostMapping("submit")
    public void submit() {
        // TODO 2025/3/27 提交对局

        // 根据对局 Key 查询库是否对手完成
        // 无匹配数据保存自身对局并生成记录


    }
}
