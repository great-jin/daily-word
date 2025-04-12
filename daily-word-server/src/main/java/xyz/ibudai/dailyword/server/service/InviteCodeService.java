package xyz.ibudai.dailyword.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.entity.InviteCode;

import java.util.List;

/**
 * (InviteCode)表服务接口
 *
 * @author budai
 * @since 2025-03-16 09:26:04
 */
public interface InviteCodeService extends IService<InviteCode> {

    /**
     * List data list.
     *
     * @return the list
     */
    List<InviteCode> listData();
}

