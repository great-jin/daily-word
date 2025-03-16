package xyz.ibudai.dailyword.repository.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.entity.MatchRecord;
import xyz.ibudai.dailyword.repository.dao.MatchRecordDao;
import xyz.ibudai.dailyword.repository.service.MatchRecordService;

/**
 * (MatchRecord)表服务实现类
 *
 * @author makejava
 * @since 2025-03-16 09:26:04
 */
@Service
public class MatchRecordServiceImpl extends ServiceImpl<MatchRecordDao, MatchRecord> implements MatchRecordService {

}

