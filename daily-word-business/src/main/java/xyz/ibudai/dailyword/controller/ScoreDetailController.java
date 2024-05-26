package xyz.ibudai.dailyword.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.ibudai.dailyword.service.ScoreDetailService;
import xyz.ibudai.dailyword.entity.ScoreDetail;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * (ScoreDetail)表控制层
 *
 * @author makejava
 * @since 2023-12-16 09:59:31
 */
@RestController
@RequestMapping("/api/server/scoreDetail")
public class ScoreDetailController {

    /**
     * 服务对象
     */
    @Autowired
    private ScoreDetailService scoreDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param scoreDetail 查询实体
     * @return 所有数据
     */
    @GetMapping("page")
    public Page<ScoreDetail> selectAll(Page<ScoreDetail> page, ScoreDetail scoreDetail) {
        return this.scoreDetailService.page(page, new QueryWrapper<>(scoreDetail));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ScoreDetail selectOne(@PathVariable Serializable id) {
        return this.scoreDetailService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param scoreDetail 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public Boolean insert(@RequestBody ScoreDetail scoreDetail) {
        return this.scoreDetailService.save(scoreDetail);
    }

    /**
     * 修改数据
     *
     * @param scoreDetail 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public Boolean update(@RequestBody ScoreDetail scoreDetail) {
        return this.scoreDetailService.updateById(scoreDetail);
    }
}

