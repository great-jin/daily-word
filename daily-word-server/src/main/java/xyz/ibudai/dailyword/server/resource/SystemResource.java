package xyz.ibudai.dailyword.server.resource;

import org.springframework.web.bind.annotation.*;

/**
 * (DictCatalog)表控制层
 *
 * @author ibudai
 * @since 2025 -03-16 09:26:03
 */
@RestController
@RequestMapping("/api/server/system")
public class SystemResource {


    /**
     * Gets catalog.
     */
    @GetMapping("getCatalog")
    public void getCatalog() {
        // TODO 2025/3/27 查询【词典】字典


    }

    /**
     * Gets rank mode.
     */
    @GetMapping("getRankMode")
    public void getRankMode() {
        // TODO 2025/3/27 查询【匹配类型】字典


    }

    /**
     * Gets room type.
     */
    @GetMapping("getRoomType")
    public void getRoomType() {
        // TODO 2025/3/27 查询【房间类型】字典


    }

    /**
     * Gets match size.
     */
    @GetMapping("getMatchSize")
    public void getMatchSize() {
        // TODO 2025/3/27 查询【每组单词数】字典


    }
}

