package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.ibudai.dailyword.files.util.FileServer;
import xyz.ibudai.dailyword.model.entity.UserDetail;
import xyz.ibudai.dailyword.model.props.FilesProps;
import xyz.ibudai.dailyword.repository.service.UserDetailService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

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

    private final FilesProps filesProps;
    private final FileServer fileServer;

    private final UserDetailService userDetailService;


    /**
     * Gets by id.
     *
     * @return the by id
     */
    @GetMapping("getById")
    public UserDetail getById() {
        UserDetail user = userDetailService.getById(SecurityUtil.getLoginUser());

        String url = fileServer.signUrl(
                user.getAvatar(),
                filesProps.getAvatarDir(),
                TimeUnit.HOURS.toMillis(1)
        );
        user.setAvatar(url);
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
        // TODO 2025/4/9 二次校验文件是否合规，是否图片


        UserDetail user = userDetailService.getById(SecurityUtil.getLoginUser());
        String avatarPath = filesProps.getHome() + File.separator + filesProps.getAvatarDir();
        File avatarFile = new File(avatarPath, user.getAvatar());
        try (
                InputStream in = file.getInputStream();
                OutputStream out = Files.newOutputStream(avatarFile.toPath())
        ) {
            fileServer.compress(in, out);
            return fileServer.signUrl(
                    user.getAvatar(),
                    filesProps.getAvatarDir(),
                    TimeUnit.HOURS.toMillis(1)
            );
        }
    }
}

