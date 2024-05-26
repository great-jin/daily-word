package xyz.ibudai.dailyword.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.ibudai.dailyword.dao.UserDetailDao;
import xyz.ibudai.dailyword.entity.UserDetail;
import xyz.ibudai.dailyword.service.UserDetailService;
import org.springframework.stereotype.Service;

/**
 * (UserDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-12-16 10:30:46
 */
@Service("userDetailService")
public class UserDetailServiceImpl extends ServiceImpl<UserDetailDao, UserDetail> implements UserDetailService {

}

