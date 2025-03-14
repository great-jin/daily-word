package xyz.ibudai.dailyword.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.*;
import xyz.ibudai.dailyword.server.cache.DicPreHeat;

import java.util.*;

/**
 * The type Word service.
 */
@Slf4j
@Service
public class WordService {

    /**
     * Gets dict detail.
     *
     * @return the dict detail
     */
    public List<DictDetail> getDictDetail() {
        List<DictDetail> detailList = new ArrayList<>();
        for (Map.Entry<Catalogue, Map<Integer, TaskWord>> Catalogue : DicPreHeat.dictCache.entrySet()) {
            Catalogue catalogue = Catalogue.getKey();
            int wordCount = Catalogue.getValue().size();
            DictDetail detail = new DictDetail(catalogue, wordCount);
            detailList.add(detail);
        }
        return detailList;
    }

    /**
     * Translation word.
     *
     * @param target the target
     * @return the word
     */
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

    /**
     * Gets task content.
     *
     * @param wordRequest the word request
     * @return the task content
     */
    public List<TaskWord> getTaskContent(WordRequest wordRequest) {
        Integer size = wordRequest.getSize();
        Integer offset = wordRequest.getOffset();
        if (Objects.isNull(size) || size <= 0) {
            size = 20;
        }

        try {
            Catalogue catalogue = wordRequest.getCatalogue();
            Collection<TaskWord> values = DicPreHeat.dictCache.get(catalogue).values();
            if (Objects.isNull(offset) || offset <= 0 || offset > values.size()) {
                throw new IllegalAccessException("Offset is illegal");
            }

            return values.stream()
                    .filter(it -> it.getOffset() >= offset)
                    .limit(size)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
