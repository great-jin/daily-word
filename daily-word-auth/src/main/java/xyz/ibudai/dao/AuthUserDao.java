package xyz.ibudai.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.ibudai.entity.AuthUser;

/**
 * (TbUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-31 14:31:28
 */
@Mapper
public interface AuthUserDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AuthUser queryById(String id);

    /**
     * Query by name tb user.
     *
     * @param userName the username
     * @return the tb user
     */
    AuthUser queryByName(String userName);

}

