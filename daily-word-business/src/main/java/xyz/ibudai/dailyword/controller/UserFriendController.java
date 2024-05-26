package xyz.ibudai.dailyword.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.ibudai.dailyword.service.UserFriendService;
import xyz.ibudai.dailyword.entity.UserFriend;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * (UserFriend)表控制层
 *
 * @author makejava
 * @since 2023-12-16 09:59:32
 */
@RestController
@RequestMapping("/api/server/userFriend")
public class UserFriendController {

    /**
     * 服务对象
     */
    @Autowired
    private UserFriendService userFriendService;

    /**
     * 分页查询所有数据
     *
     * @param page       分页对象
     * @param userFriend 查询实体
     * @return 所有数据
     */
    @GetMapping
    public Page<UserFriend> selectAll(Page<UserFriend> page, UserFriend userFriend) {
        return this.userFriendService.page(page, new QueryWrapper<>(userFriend));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public Serializable selectOne(@PathVariable Serializable id) {
        return this.userFriendService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param userFriend 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public Boolean insert(@RequestBody UserFriend userFriend) {
        return this.userFriendService.save(userFriend);
    }

    /**
     * 修改数据
     *
     * @param userFriend 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public Boolean update(@RequestBody UserFriend userFriend) {
        return this.userFriendService.updateById(userFriend);
    }
}

