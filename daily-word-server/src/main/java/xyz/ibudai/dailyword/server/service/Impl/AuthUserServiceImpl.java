package xyz.ibudai.dailyword.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.repository.dao.AuthUserDao;
import xyz.ibudai.dailyword.server.service.AuthUserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthUserServiceImpl extends ServiceImpl<AuthUserDao, AuthUser> implements AuthUserService {

    @Override
    public AuthUser queryByName(String userName) {
        return this.baseMapper.queryByName(userName);
    }

    @Override
    public Map<Integer, String> groupByIds(Collection<Integer> ids) {
        List<AuthUser> authUsers = this.baseMapper.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(authUsers)) {
            return new HashMap<>();
        }

        return authUsers.stream()
                .collect(Collectors.toMap(
                        AuthUser::getId,
                        AuthUser::getUsername,
                        // keep old data
                        (o, r) -> o)
                );
    }
}
