package xyz.ibudai.dailyword.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.ibudai.dailyword.dao.UserFriendDao;
import xyz.ibudai.dailyword.service.UserFriendService;
import xyz.ibudai.dailyword.entity.UserFriend;
import org.springframework.stereotype.Service;

/**
 * (UserFriend)表服务实现类
 *
 * @author makejava
 * @since 2023-12-16 09:59:32
 */
@Service("userFriendService")
public class UserFriendServiceImpl extends ServiceImpl<UserFriendDao, UserFriend> implements UserFriendService {

}

