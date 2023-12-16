package xyz.ibudai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.ibudai.dao.WordPlanDao;
import xyz.ibudai.entity.WordPlan;
import xyz.ibudai.service.WordPlanService;
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

