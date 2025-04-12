package xyz.ibudai.dailyword.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.entity.user.AuthUser;

import java.util.Collection;
import java.util.Map;

/**
 * The interface Auth user service.
 */
public interface AuthUserService extends IService<AuthUser> {

    /**
     * Query by name auth user.
     *
     * @param userName the username
     * @return the auth user
     */
    AuthUser queryByName(String userName);

    /**
     * Group by ids map.
     *
     * @param ids the ids
     * @return the map
     */
    Map<Integer, String> groupByIds(Collection<Integer> ids);

}
