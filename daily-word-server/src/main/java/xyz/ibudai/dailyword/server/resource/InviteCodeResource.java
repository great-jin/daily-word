package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.dailyword.basic.tool.CodeTool;
import xyz.ibudai.dailyword.model.entity.InviteCode;
import xyz.ibudai.dailyword.model.enums.InviteCodeStatus;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;
import xyz.ibudai.dailyword.server.service.InviteCodeService;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Invite code resource.
 */
@RestController
@RequestMapping("/api/server/inviteCode")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InviteCodeResource {

    private final InviteCodeService inviteCodeService;


    /**
     * List data.
     *
     * @return the list
     */
    @GetMapping("list")
    public List<InviteCode> list() {
        return inviteCodeService.listData();
    }

    /**
     * Generate boolean.
     *
     * @return the boolean
     */
    @GetMapping("generate")
    public Boolean generate() {
        InviteCode inviteCode = InviteCode.init(SecurityUtil.getLoginUser());
        inviteCode.setCode(CodeTool.generate());
        return inviteCodeService.save(inviteCode);
    }

    /**
     * Delete code boolean.
     *
     * @param id the id
     * @return the boolean
     */
    @GetMapping("delete")
    public Boolean deleteCode(@RequestParam("id") Integer id) {
        return inviteCodeService.removeById(id);
    }

    /**
     * Re active.
     *
     * @param id the id
     * @return the boolean
     */
    @GetMapping("reactive")
    public Boolean reactive(@RequestParam("id") Integer id) {
        return inviteCodeService.lambdaUpdate()
                .set(InviteCode::getStatus, InviteCodeStatus.UN_USE.getStatus())
                // 过期时间重新后移 7 天
                .set(InviteCode::getExpireDate, LocalDate.now().plusDays(7))
                .eq(InviteCode::getId, id)
                .update();
    }
}
