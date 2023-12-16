package xyz.ibudai.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.ibudai.entity.UserDetail;
import xyz.ibudai.service.UserDetailService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (UserDetail)表控制层
 *
 * @author makejava
 * @since 2023-12-16 10:30:46
 */
@RestController
@RequestMapping("/api/auth/user")
public class UserDetailController {

    /**
     * 服务对象
     */
    @Autowired
    private UserDetailService userDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page       分页对象
     * @param userDetail 查询实体
     * @return 所有数据
     */
    @GetMapping("page")
    public Page<UserDetail> selectAll(Page<UserDetail> page, UserDetail userDetail) {
        return this.userDetailService.page(page, new QueryWrapper<>(userDetail));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public UserDetail selectOne(@PathVariable Serializable id) {
        return this.userDetailService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param userDetail 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public Boolean insert(@RequestBody UserDetail userDetail) {
        return this.userDetailService.save(userDetail);
    }

    /**
     * 修改数据
     *
     * @param userDetail 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public Boolean update(@RequestBody UserDetail userDetail) {
        return this.userDetailService.updateById(userDetail);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @PostMapping("delete")
    public Boolean delete(@RequestParam("idList") List<Long> idList) {
        return this.userDetailService.removeByIds(idList);
    }
}

