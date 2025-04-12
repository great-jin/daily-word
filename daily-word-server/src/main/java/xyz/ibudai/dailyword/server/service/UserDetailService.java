package xyz.ibudai.dailyword.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import xyz.ibudai.dailyword.model.entity.user.UserDetail;

import java.util.List;
import java.util.Map;

/**
 * (UserDetail)表服务接口
 *
 * @author budai
 * @since 2025-03-16 09:26:05
 */
public interface UserDetailService extends IService<UserDetail> {

    /**
     * Group by user id map.
     *
     * @param userIds the user ids
     * @return the map
     */
    Map<Integer, UserDetail> groupByUserId(List<Integer> userIds);

    /**
     * Upload avatar string.
     *
     * @param file the file
     * @return the string
     */
    String uploadAvatar(MultipartFile file);
}

