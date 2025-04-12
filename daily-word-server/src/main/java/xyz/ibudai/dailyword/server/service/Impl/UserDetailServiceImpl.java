package xyz.ibudai.dailyword.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.entity.user.UserDetail;
import xyz.ibudai.dailyword.repository.dao.UserDetailDao;
import xyz.ibudai.dailyword.server.service.UserDetailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (UserDetail)表服务实现类
 *
 * @author budai
 * @since 2025-03-16 09:26:05
 */
@Service
public class UserDetailServiceImpl extends ServiceImpl<UserDetailDao, UserDetail> implements UserDetailService {


    @Override
    public Map<Integer, UserDetail> groupByUserId(List<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new HashMap<>();
        }

        Map<Integer, UserDetail> userDetailMap = new HashMap<>();
        List<UserDetail> list = this.lambdaQuery()
                .in(UserDetail::getUserId, userIds)
                .list();
        list.forEach(userDetail -> userDetailMap.put(userDetail.getUserId(), userDetail));
        return userDetailMap;
    }
}

