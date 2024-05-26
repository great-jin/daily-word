package xyz.ibudai.dailyword.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.ibudai.dailyword.entity.WordPlan;
import xyz.ibudai.dailyword.service.WordPlanService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * (WordPlan)表控制层
 *
 * @author makejava
 * @since 2023-12-16 09:59:32
 */
@RestController
@RequestMapping("/api/server/wordPlan")
public class WordPlanController {

    /**
     * 服务对象
     */
    @Autowired
    private WordPlanService wordPlanService;

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param wordPlan 查询实体
     * @return 所有数据
     */
    @GetMapping("page")
    public Page<WordPlan> selectAll(Page<WordPlan> page, WordPlan wordPlan) {
        return this.wordPlanService.page(page, new QueryWrapper<>(wordPlan));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public WordPlan selectOne(@RequestParam("id") Serializable id) {
        return this.wordPlanService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param wordPlan 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public Boolean insert(@RequestBody WordPlan wordPlan) {
        return this.wordPlanService.save(wordPlan);
    }

    /**
     * 修改数据
     *
     * @param wordPlan 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public Boolean update(@RequestBody WordPlan wordPlan) {
        return this.wordPlanService.updateById(wordPlan);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果 boolean
     */
    @GetMapping("/delete/{id}")
    public Boolean delete(@PathVariable Serializable id) {
        return this.wordPlanService.removeById(id);
    }
}

