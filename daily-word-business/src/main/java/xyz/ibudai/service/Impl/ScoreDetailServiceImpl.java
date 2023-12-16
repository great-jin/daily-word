package xyz.ibudai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.ibudai.dao.ScoreDetailDao;
import xyz.ibudai.entity.ScoreDetail;
import xyz.ibudai.service.ScoreDetailService;
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

