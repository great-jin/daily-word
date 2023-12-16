package xyz.ibudai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.ibudai.dao.UserFriendDao;
import xyz.ibudai.entity.UserFriend;
import xyz.ibudai.service.UserFriendService;
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

