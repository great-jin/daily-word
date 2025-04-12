package xyz.ibudai.dailyword.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import xyz.ibudai.dailyword.basic.encrypt.SHAUtil;
import xyz.ibudai.dailyword.basic.enums.FileType;
import xyz.ibudai.dailyword.model.dto.PasswordDTO;
import xyz.ibudai.dailyword.model.entity.user.AuthUser;
import xyz.ibudai.dailyword.model.entity.user.UserDetail;
import xyz.ibudai.dailyword.model.props.OssProps;
import xyz.ibudai.dailyword.oss.util.OssServer;
import xyz.ibudai.dailyword.repository.dao.UserDetailDao;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;
import xyz.ibudai.dailyword.server.service.AuthUserService;
import xyz.ibudai.dailyword.server.service.UserDetailService;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * (UserDetail)表服务实现类
 *
 * @author budai
 * @since 2025-03-16 09:26:05
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailServiceImpl extends ServiceImpl<UserDetailDao, UserDetail> implements UserDetailService {

    private final OssProps ossProps;
    private final OssServer ossServer;

    private final AuthUserService authUserService;


    @Override
    public Map<Integer, UserDetail> groupByUserId(List<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new HashMap<>();
        }

        Map<Integer, UserDetail> userDetailMap = new HashMap<>();
        List<UserDetail> list = this.lambdaQuery()
                .in(UserDetail::getUserId, userIds)
                .list();
        list.forEach(userDetail -> userDetailMap.put(userDetail.getUserId(), userDetail));
        return userDetailMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(MultipartFile file) {
        Integer loginUser = SecurityUtil.getLoginUser();
        UserDetail user = this.getById(loginUser);
        String avatarPath = user.getAvatar();
        if (StringUtils.isBlank(avatarPath)) {
            String filename = UUID.nameUUIDFromBytes(loginUser.toString().getBytes()).toString();
            avatarPath = filename + FileType.JPEG.getExtension();

            // 更新头像
            this.lambdaUpdate()
                    .set(UserDetail::getAvatar, avatarPath)
                    .eq(UserDetail::getUserId, loginUser)
                    .update();
        }

        // 生成 URL
        String fullDir = ossProps.getHome() + File.separator + ossProps.getAvatarDir();
        File avatarFile = new File(fullDir, avatarPath);
        try (
                InputStream in = file.getInputStream();
                OutputStream out = Files.newOutputStream(avatarFile.toPath())
        ) {
            ossServer.compress(in, out);
            return ossServer.signAvatarUrl(avatarPath);
        } catch (Exception e) {
            throw new RuntimeException("Upload avatar failed", e);
        }
    }

    @Override
    public Integer changePassword(PasswordDTO dto) throws NoSuchAlgorithmException {
        AuthUser user = authUserService.getById(SecurityUtil.getLoginUser());
        String hashPwd = SHAUtil.hash(user.getPassword());
        if (!Objects.equals(hashPwd, dto.getOriginPwd())) {
            // 旧密码错误
            return 1;
        }
        if (Objects.equals(hashPwd, dto.getPassword())) {
            // 新旧密码一致
            return 2;
        }

        boolean success = authUserService.lambdaUpdate()
                .set(AuthUser::getPassword, dto.getPassword())
                .eq(AuthUser::getId, user.getId())
                .update();
        return success ? 3 : 4;
    }
}

