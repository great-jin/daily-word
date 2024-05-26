package xyz.ibudai.dailyword.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.ibudai.dailyword.dao.WordPlanDao;
import xyz.ibudai.dailyword.service.WordPlanService;
import xyz.ibudai.dailyword.entity.WordPlan;
import org.springframework.stereotype.Service;

/**
 * (WordPlan)表服务实现类
 *
 * @author makejava
 * @since 2023-12-16 09:59:32
 */
@Service("wordPlanService")
public class WordPlanServiceImpl extends ServiceImpl<WordPlanDao, WordPlan> implements WordPlanService {

}

