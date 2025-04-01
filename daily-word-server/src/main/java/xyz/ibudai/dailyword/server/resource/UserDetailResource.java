package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.entity.UserDetail;
import xyz.ibudai.dailyword.repository.service.UserDetailService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;

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

    private final UserDetailService userDetailService;


    /**
     * Gets by id.
     *
     * @return the by id
     */
    @GetMapping("get")
    public UserDetail getById() {
        return userDetailService.getById(SecurityUtil.getLoginUser());
    }
}

