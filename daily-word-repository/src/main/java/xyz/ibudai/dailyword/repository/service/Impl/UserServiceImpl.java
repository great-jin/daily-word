package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.entity.AuthUser;
import xyz.ibudai.dailyword.repository.mapper.UserMapper;
import xyz.ibudai.dailyword.repository.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, AuthUser> implements UserService {

    @Override
    public AuthUser queryByName(String userName) {
        return this.baseMapper.queryByName(userName);
    }
}
