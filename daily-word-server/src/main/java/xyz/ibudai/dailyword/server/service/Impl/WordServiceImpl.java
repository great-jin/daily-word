package xyz.ibudai.dailyword.server.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.DictDetail;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.vo.word.Word;
import xyz.ibudai.dailyword.model.vo.word.WordDescribe;
import xyz.ibudai.dailyword.server.cache.DicPreHeat;
import xyz.ibudai.dailyword.server.service.WordService;

import java.util.*;

@Slf4j
@Service
public class WordServiceImpl implements WordService {

    public List<DictDetail> getDictDetail() {
        List<DictDetail> detailList = new ArrayList<>();
        for (Map.Entry<xyz.ibudai.dailyword.model.enums.Catalogue, Map<Integer, TaskWordDTO>> Catalogue : DicPreHeat.dictCache.entrySet()) {
            Catalogue catalogue = Catalogue.getKey();
            int wordCount = Catalogue.getValue().size();
            DictDetail detail = new DictDetail(catalogue, wordCount);
            detailList.add(detail);
        }
        return detailList;
    }

    public Word translation(String target) {
        String targetLowCase = target.toLowerCase(Locale.ROOT);
        List<JsonNode> nodeList = DicPreHeat.catalogueMap.get(targetLowCase);
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

    public List<TaskWordDTO> getTaskContent(Catalogue catalogue, Integer size) {
        if (Objects.isNull(size) || size <= 0) {
            size = 20;
        }

        try {
            Collection<TaskWordDTO> values = DicPreHeat.dictCache.get(catalogue).values();
            return values.stream()
                    // TODO 2025/3/25 随机生成偏移量
                    .filter(it -> it.getOffset() >= 0)
                    .limit(size)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
