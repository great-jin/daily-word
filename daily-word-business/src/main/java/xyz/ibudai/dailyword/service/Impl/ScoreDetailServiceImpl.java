package xyz.ibudai.dailyword.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.ibudai.dailyword.dao.ScoreDetailDao;
import xyz.ibudai.dailyword.service.ScoreDetailService;
import xyz.ibudai.dailyword.entity.ScoreDetail;
import org.springframework.stereotype.Service;

/**
 * (ScoreDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-12-16 09:59:31
 */
@Service("scoreDetailService")
public class ScoreDetailServiceImpl extends ServiceImpl<ScoreDetailDao, ScoreDetail> implements ScoreDetailService {

}

