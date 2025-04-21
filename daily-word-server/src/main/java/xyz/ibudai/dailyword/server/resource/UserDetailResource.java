package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.ibudai.dailyword.model.base.ResponseData;
import xyz.ibudai.dailyword.model.dto.PasswordDTO;
import xyz.ibudai.dailyword.model.enums.status.PasswordStatus;
import xyz.ibudai.dailyword.oss.util.OssServer;
import xyz.ibudai.dailyword.model.entity.user.UserDetail;
import xyz.ibudai.dailyword.server.service.UserDetailService;
import xyz.ibudai.dailyword.auth.util.SecurityUtil;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

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
     */
    @PostMapping("uploadAvatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) {
        return userDetailService.uploadAvatar(file);
    }

    /**
     * Change password integer.
     *
     * @param dto the dto
     * @return the integer
     */
    @PostMapping("changePassword")
    public ResponseData changePassword(@RequestBody PasswordDTO dto) throws NoSuchAlgorithmException {
        PasswordStatus status = userDetailService.changePassword(dto);
        if (!Objects.equals(status, PasswordStatus.SUCCESS)) {
            return ResponseData.failed(status.getMessage());
        }

        return ResponseData.success(true);
    }
}

