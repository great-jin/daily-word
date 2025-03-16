package xyz.ibudai.dailyword.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.entity.RankBoard;
import xyz.ibudai.dailyword.repository.service.RankBoardService;

import java.util.List;

/**
 * (RankBoard)表控制层
 *
 * @author ibudai
 * @since 2025 -03-16 09:26:04
 */
@RestController
@RequestMapping("/api/server/rankBoard")
public class RankBoardResource {

    /**
     * 服务对象
     */
    @Autowired
    private RankBoardService rankBoardService;


    /**
     * 查询前 100 条记录
     *
     * @param catalog the catalog
     * @return 所有数据 list
     */
    @GetMapping("list")
    public List<RankBoard> list(Integer userId, String catalog) {
        return rankBoardService.listByCatalog(userId, catalog);
    }

    /**
     * 查询用户记录
     *
     * @param userId the user id
     * @return 所有数据 list
     */
    @GetMapping("getUserRank")
    public RankBoard getUserRank(Integer userId) {
        return rankBoardService.getUserRank(userId);
    }
}

