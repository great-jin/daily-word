package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
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
    public List<RankBoard> list(String catalog) {
        return rankBoardService.listByCatalog(catalog);
    }

    /**
     * 查询用户记录
     *
     * @return 所有数据 list
     */
    @GetMapping("getUserRank")
    public RankBoard getUserRank() {
        return rankBoardService.getUserRank();
    }
}

