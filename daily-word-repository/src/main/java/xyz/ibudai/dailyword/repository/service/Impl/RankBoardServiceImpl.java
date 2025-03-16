package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.entity.RankBoard;
import xyz.ibudai.dailyword.repository.dao.RankBoardDao;
import xyz.ibudai.dailyword.repository.service.RankBoardService;

import java.util.List;

/**
 * (RankBoard)表服务实现类
 *
 * @author makejava
 * @since 2025-03-16 09:26:04
 */
@Service
public class RankBoardServiceImpl extends ServiceImpl<RankBoardDao, RankBoard> implements RankBoardService {


    @Override
    public List<RankBoard> listByCatalog(String catalog) {
        return this.lambdaQuery()
                .eq(RankBoard::getSeason, 1)
                .eq(RankBoard::getCatalog, catalog)
                .list();
    }
}

