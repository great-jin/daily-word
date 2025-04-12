package xyz.ibudai.dailyword.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.entity.match.MatchDetail;
import xyz.ibudai.dailyword.repository.dao.MatchDetailDao;
import xyz.ibudai.dailyword.server.service.MatchDetailService;

/**
 * (MatchDetail)表服务实现类
 *
 * @author budai
 * @since 2025-04-06 21:10:47
 */
@Service
public class MatchDetailServiceImpl extends ServiceImpl<MatchDetailDao, MatchDetail> implements MatchDetailService {

}

