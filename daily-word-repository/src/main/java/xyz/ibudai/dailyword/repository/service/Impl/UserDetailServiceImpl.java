package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.entity.UserDetail;
import xyz.ibudai.dailyword.repository.dao.UserDetailDao;
import xyz.ibudai.dailyword.repository.service.UserDetailService;

/**
 * (UserDetail)表服务实现类
 *
 * @author makejava
 * @since 2025-03-16 09:26:05
 */
@Service
public class UserDetailServiceImpl extends ServiceImpl<UserDetailDao, UserDetail> implements UserDetailService {

}

