package xyz.ibudai.dailyword.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ibudai.dailyword.model.entity.RankBoard;

import java.util.List;

/**
 * (RankBoard)表服务接口
 *
 * @author makejava
 * @since 2025-03-16 09:26:04
 */
public interface RankBoardService extends IService<RankBoard> {

    List<RankBoard> listByCatalog(String catalog);
}

