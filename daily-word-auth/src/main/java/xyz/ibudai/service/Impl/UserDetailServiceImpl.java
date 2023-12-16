package xyz.ibudai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.ibudai.dao.UserDetailDao;
import xyz.ibudai.entity.UserDetail;
import xyz.ibudai.service.UserDetailService;
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

