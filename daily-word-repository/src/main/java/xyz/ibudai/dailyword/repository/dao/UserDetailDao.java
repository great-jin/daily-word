package xyz.ibudai.dailyword.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.ibudai.dailyword.model.entity.UserDetail;

/**
 * (UserDetail)表数据库访问层
 *
 * @author budai
 * @since 2025-03-16 09:26:05
 */
public interface UserDetailDao extends BaseMapper<UserDetail> {

    /**
     * Update online time.
     *
     * @param userId the user id
     */
    void updateOnlineTime(Integer userId);
}

