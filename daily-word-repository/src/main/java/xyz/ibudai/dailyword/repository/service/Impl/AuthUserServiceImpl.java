package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.repository.dao.AuthUserDao;
import xyz.ibudai.dailyword.repository.service.AuthUserService;

@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserDao, AuthUser> implements AuthUserService {

    @Override
    public AuthUser queryByName(String userName) {
        return this.baseMapper.queryByName(userName);
    }
}
