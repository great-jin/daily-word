package xyz.ibudai.dailyword.repository.dao.impl;

import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.repository.dao.AbstractDao;
import xyz.ibudai.dailyword.repository.entity.AuthUser;

import java.sql.SQLException;

/**
 * (TbUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-31 14:31:28
 */
@Service("userDao")
public class UserDao extends AbstractDao<AuthUser, Long> {

    /**
     * Query by name tb user.
     *
     * @param userName the username
     * @return the tb user
     */
    public AuthUser queryByName(String userName) {
        try {
            AuthUser authUser = new AuthUser();
            authUser.setUsername(userName);
            return this.findFirst(authUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

