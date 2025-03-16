package xyz.ibudai.dailyword.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.ibudai.dailyword.model.entity.AuthUser;

public interface AuthUserDao extends BaseMapper<AuthUser> {

    /**
     * Query by name tb user.
     *
     * @param userName the username
     * @return the tb user
     */
    AuthUser queryByName(String userName);
}
