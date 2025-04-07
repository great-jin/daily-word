package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.files.util.FileServer;
import xyz.ibudai.dailyword.model.entity.UserDetail;
import xyz.ibudai.dailyword.model.props.FilesProps;
import xyz.ibudai.dailyword.repository.service.UserDetailService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

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
    @GetMapping("get")
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
}

