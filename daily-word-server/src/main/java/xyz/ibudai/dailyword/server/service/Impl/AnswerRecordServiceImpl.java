package xyz.ibudai.dailyword.server.service.Impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.entity.match.MatchDetail;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.mongo.AnswerRecord;
import xyz.ibudai.dailyword.model.mongo.SubjectContent;
import xyz.ibudai.dailyword.repository.dao.MatchDetailDao;
import xyz.ibudai.dailyword.repository.mongo.MongoRepository;
import xyz.ibudai.dailyword.auth.util.SecurityUtil;
import xyz.ibudai.dailyword.server.tool.DictionaryTool;
import xyz.ibudai.dailyword.server.service.AnswerRecordService;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnswerRecordServiceImpl implements AnswerRecordService {

    private final MongoRepository mongoRepository;

    private final MatchDetailDao matchDetailDao;


    @Override
    public int getCorrectCount(Integer matchId, List<SubjectContent> contentList) {
        if (Objects.isNull(contentList)) {
            // 默认空集合
            contentList = new ArrayList<>();
        }

        // 查询答案
        MatchDetail matchDetail = matchDetailDao.selectById(matchId);
        Catalogue catalogue = Catalogue.valueOf(matchDetail.getCatalog());
        List<TaskWordDTO> answers = DictionaryTool.extract(catalogue, matchDetail.getWordIndies());

        // 内容匹配
        int count = 0;
        Map<Integer, String> answerMap = new HashMap<>(answers.size());
        answers.forEach(answer -> answerMap.put(answer.getOffset(), answer.getValue()));
        for (SubjectContent item : contentList) {
            Integer offset = item.getOffset();
            String answer = answerMap.get(offset);
            if (Objects.equals(answer, item.getAnswer())) {
                count++;
            }
        }

        // 过滤空白输入
        contentList = contentList.stream()
                .filter(it -> {
                    String answer = it.getAnswer();
                    if (StringUtils.isBlank(answer)) {
                        return false;
                    }

                    return StringUtils.isBlank(answer.trim());
                })
                .toList();

        // 转存 Mongo
        AnswerRecord answerRecord = new AnswerRecord();
        answerRecord.setMatchId(matchId);
        answerRecord.setUserId(SecurityUtil.getLoginUser());
        answerRecord.setSubjectList(contentList);
        mongoRepository.insert(answerRecord);
        return count;
    }
}
