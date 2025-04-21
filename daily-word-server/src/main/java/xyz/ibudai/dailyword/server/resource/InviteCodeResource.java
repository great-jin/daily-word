package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.dailyword.basic.tool.CodeTool;
import xyz.ibudai.dailyword.model.entity.InviteCode;
import xyz.ibudai.dailyword.model.enums.status.InviteCodeStatus;
import xyz.ibudai.dailyword.auth.util.SecurityUtil;
import xyz.ibudai.dailyword.server.service.InviteCodeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
        List<InviteCode> userCodeList = inviteCodeService.lambdaQuery()
                .eq(InviteCode::getUserId, SecurityUtil.getLoginUser())
                .list();
        if (!CollectionUtils.isEmpty(userCodeList) && userCodeList.size() > 6) {
            // 限制 6 个邀请码
            return false;
        }
        String code = CodeTool.generate();
        List<InviteCode> codeList = inviteCodeService.lambdaQuery()
                .eq(InviteCode::getCode, code)
                .list();
        if (!CollectionUtils.isEmpty(codeList)) {
            // 重复，生成失败
            return false;
        }

        InviteCode inviteCode = InviteCode.init(SecurityUtil.getLoginUser());
        inviteCode.setCode(code);
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
        InviteCode inviteCode = inviteCodeService.lambdaQuery()
                .eq(InviteCode::getId, id)
                .eq(InviteCode::getUserId, SecurityUtil.getLoginUser())
                .one();
        if (Objects.equals(inviteCode.getStatus(), InviteCodeStatus.USED.getStatus())) {
            return false;
        }

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
