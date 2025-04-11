package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.entity.RankBoard;
import xyz.ibudai.dailyword.model.vo.board.RankBoardVo;
import xyz.ibudai.dailyword.server.service.RankBoardService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.util.List;

/**
 * (RankBoard)表控制层
 *
 * @author ibudai
 * @since 2025 -03-16 09:26:04
 */
@RestController
@RequestMapping("/api/server/rankBoard")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankBoardResource {

    private final RankBoardService rankBoardService;


    /**
     * 查询前 100 条记录
     *
     * @param catalog the catalog
     * @return 所有数据 list
     */
    @GetMapping("list")
    public List<RankBoardVo> list(String catalog) {
        return rankBoardService.listByCatalog(catalog);
    }

    /**
     * 查询登录用户记录
     *
     * @return 用户记录
     */
    @GetMapping("getUserRank")
    public RankBoard getUserRank() {
        return rankBoardService.getUserRank(SecurityUtil.getLoginUser());
    }

    /**
     * 查询用户记录
     *
     * @return 用户记录
     */
    @GetMapping("getRankByUid")
    public RankBoard getRankByUid(@RequestParam("userId") Integer userId) {
        return rankBoardService.getUserRank(userId);
    }
}

