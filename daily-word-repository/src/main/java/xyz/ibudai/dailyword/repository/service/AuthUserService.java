package xyz.ibudai.dailyword.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.entity.AuthUser;

public interface AuthUserService extends IService<AuthUser> {

    AuthUser queryByName(String userName);
}
