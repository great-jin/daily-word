package xyz.ibudai.dailyword.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.entity.InviteCode;
import xyz.ibudai.dailyword.model.enums.status.InviteCodeStatus;
import xyz.ibudai.dailyword.repository.dao.InviteCodeDao;
import xyz.ibudai.dailyword.auth.util.SecurityUtil;
import xyz.ibudai.dailyword.server.service.InviteCodeService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * (InviteCode)表服务实现类
 *
 * @author budai
 * @since 2025-03-16 09:26:04
 */
@Service
public class InviteCodeServiceImpl extends ServiceImpl<InviteCodeDao, InviteCode> implements InviteCodeService {


    @Override
    public List<InviteCode> listData() {
        List<InviteCode> list = this.lambdaQuery()
                .eq(InviteCode::getUserId, SecurityUtil.getLoginUser())
                .list();

        // 状态实时处理
        Set<Integer> expireIds = new HashSet<>();
        LocalDate now = LocalDate.now();
        for (InviteCode item : list) {
            if (now.isAfter(item.getExpireDate())) {
                expireIds.add(item.getId());
                item.setStatus(InviteCodeStatus.EXPIRE.getStatus());
            }
        }

        // 更新过期状态
        if (!CollectionUtils.isEmpty(expireIds)) {
            this.lambdaUpdate()
                    .set(InviteCode::getStatus, InviteCodeStatus.EXPIRE.getStatus())
                    .in(InviteCode::getId, expireIds)
                    .update();
        }

        return list;
    }
}

