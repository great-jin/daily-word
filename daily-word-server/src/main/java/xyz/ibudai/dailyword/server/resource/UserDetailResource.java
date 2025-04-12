package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.ibudai.dailyword.oss.util.OssServer;
import xyz.ibudai.dailyword.model.entity.user.UserDetail;
import xyz.ibudai.dailyword.model.props.OssProps;
import xyz.ibudai.dailyword.server.service.UserDetailService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.io.*;
import java.nio.file.Files;

/**
 * (UserDetail)表控制层
 *
 * @author ibudai
 * @since 2025 -03-16 09:26:04
 */
@RestController
@RequestMapping("/api/server/userDetail")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailResource {

    private final OssProps ossProps;
    private final OssServer ossServer;

    private final UserDetailService userDetailService;


    /**
     * Gets by id.
     *
     * @return the by id
     */
    @GetMapping("getById")
    public UserDetail getById() {
        UserDetail user = userDetailService.getById(SecurityUtil.getLoginUser());
        user.setAvatar(ossServer.signAvatarUrl(user.getAvatar()));
        return user;
    }

    /**
     * Upload avatar.
     *
     * @param file the file
     * @return the by id
     * @throws IOException the io exception
     */
    @PostMapping("uploadAvatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        UserDetail user = userDetailService.getById(SecurityUtil.getLoginUser());
        String avatarPath = ossProps.getHome() + File.separator + ossProps.getAvatarDir();
        File avatarFile = new File(avatarPath, user.getAvatar());
        try (
                InputStream in = file.getInputStream();
                OutputStream out = Files.newOutputStream(avatarFile.toPath())
        ) {
            ossServer.compress(in, out);
            return ossServer.signAvatarUrl(user.getAvatar());
        }
    }
}

