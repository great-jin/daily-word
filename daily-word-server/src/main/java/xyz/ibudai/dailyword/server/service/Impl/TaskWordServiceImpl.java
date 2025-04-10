package xyz.ibudai.dailyword.server.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.basic.tool.CollTool;
import xyz.ibudai.dailyword.model.dto.RoomDTO;
import xyz.ibudai.dailyword.model.entity.MatchDetail;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.mongo.AnswerRecord;
import xyz.ibudai.dailyword.model.mongo.SubjectContent;
import xyz.ibudai.dailyword.model.vo.AnswerVo;
import xyz.ibudai.dailyword.model.vo.DictDetail;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.vo.match.MatchVo;
import xyz.ibudai.dailyword.model.vo.word.Word;
import xyz.ibudai.dailyword.model.vo.word.WordDescribe;
import xyz.ibudai.dailyword.repository.dao.MatchDetailDao;
import xyz.ibudai.dailyword.repository.mongo.MongoRepository;
import xyz.ibudai.dailyword.server.cache.DictTool;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;
import xyz.ibudai.dailyword.server.cache.DicPreHeat;
import xyz.ibudai.dailyword.server.service.MatchRecordService;
import xyz.ibudai.dailyword.server.service.TaskWordService;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskWordServiceImpl implements TaskWordService {

    private final MongoRepository mongoRepository;

    private final MatchDetailDao matchDetailDao;
    private final MatchRecordService matchRecordService;


    public List<DictDetail> getDictDetail() {
        List<DictDetail> detailList = new ArrayList<>();
        for (Map.Entry<Catalogue, Map<Integer, TaskWordDTO>> Catalogue : DicPreHeat.DICT_CACHE.entrySet()) {
            Catalogue catalogue = Catalogue.getKey();
            int wordCount = Catalogue.getValue().size();
            DictDetail detail = new DictDetail(catalogue, wordCount);
            detailList.add(detail);
        }
        return detailList;
    }

    public Word translation(String target) {
        String targetLowCase = target.toLowerCase(Locale.ROOT);
        List<JsonNode> nodeList = DicPreHeat.CATALOG_MAP.get(targetLowCase);
        if (Objects.isNull(nodeList)) {
            return Word.notFound(target);
        }

        try {
            List<WordDescribe> describeList = new ArrayList<>();
            for (JsonNode node : nodeList) {
                String name = node.get("name").asText();
                if (!name.equalsIgnoreCase(targetLowCase)) {
                    continue;
                }

                for (JsonNode jsonNode : node.get("describerList")) {
                    String type = jsonNode.get("type").asText();
                    String describe = jsonNode.get("describe").toString();
                    describe = describe.replace("\"", "");
                    describeList.add(new WordDescribe(type, describe));
                }
            }

            Word word = new Word();
            word.setExisted(true);
            word.setValue(target);
            word.setDescribeList(describeList);
            return word;
        } catch (Exception e) {
            log.error("字典查询异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public MatchVo starTask(Catalogue catalogue, Integer size) {
        Set<Integer> users = Set.of(SecurityUtil.getLoginUser());
        // 房间信息
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomSize(0);
        roomDTO.setCatalogue(catalogue);
        roomDTO.setSize(size);

        List<TaskWordDTO> dataList = this.getTaskContent(catalogue, size);

        // 初始化记录
        List<Integer> offsets = dataList.stream().map(TaskWordDTO::getOffset).toList();
        roomDTO.setWordIndies(StringUtils.join(offsets, ","));
        Integer matchId = matchRecordService.initRecord(users, roomDTO);

        return new MatchVo(matchId, dataList);
    }

    public List<TaskWordDTO> getTaskContent(Catalogue catalogue, Integer size) {
        if (Objects.isNull(size) || size <= 0) {
            size = 20;
        }

        Collection<TaskWordDTO> collection = DicPreHeat.DICT_CACHE
                .get(catalogue)
                .values();
        List<TaskWordDTO> extract = DictTool.extract(catalogue, CollTool.randoms(collection, size));
        for (TaskWordDTO item : extract) {
            item.setWordLength(item.getValue().length());
        }
        return extract;
    }

    @Override
    public AnswerVo getAnswer(Integer matchId) {
        MatchDetail matchDetail = matchDetailDao.selectById(matchId);
        if (Objects.isNull(matchDetail)) {
            return null;
        }

        // 答案
        Catalogue catalogue = Catalogue.valueOf(matchDetail.getCatalog());
        List<TaskWordDTO> answers = DictTool.extract(catalogue, matchDetail.getWordIndies());

        // 用户提交
        Map<String, Object> condition = new HashMap<>();
        condition.put("match_id", matchId);
        condition.put("user_id", SecurityUtil.getLoginUser());
        AnswerRecord answerRecord = mongoRepository.find(AnswerRecord.class, condition);
        List<SubjectContent> submits = answerRecord.getSubjectList();

        // 是否答对
        Map<Integer, String> submitMap = new HashMap<>();
        submits.forEach(it -> submitMap.put(it.getOffset(), it.getAnswer()));
        for (TaskWordDTO answer : answers) {
            answer.setCorrect(Objects.equals(answer.getValue(), submitMap.get(answer.getOffset())));
        }

        // 构建 VO
        AnswerVo answerVo = new AnswerVo();
        answerVo.setAnswers(answers);
        answerVo.setSubmits(submits);
        return answerVo;
    }
}
