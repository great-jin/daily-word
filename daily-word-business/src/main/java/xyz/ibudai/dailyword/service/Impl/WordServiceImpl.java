package xyz.ibudai.dailyword.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.cache.DicPreHeat;
import xyz.ibudai.dailyword.model.*;
import xyz.ibudai.dailyword.model.common.Catalogue;
import xyz.ibudai.dailyword.service.WordService;

import java.util.*;

@Slf4j
@Service
public class WordServiceImpl implements WordService {

    @Override
    public List<DictDetail> getDictDetail() {
        List<DictDetail> detailList = new ArrayList<>();
        for (Map.Entry<xyz.ibudai.dailyword.model.common.Catalogue, Map<Integer, TaskWord>> Catalogue : DicPreHeat.dictCache.entrySet()) {
            Catalogue catalogue = Catalogue.getKey();
            int wordCount = Catalogue.getValue().size();
            DictDetail detail = new DictDetail(catalogue, wordCount);
            detailList.add(detail);
        }
        return detailList;
    }

    @Override
    public Word translation(String target) {
        boolean existed;
        List<WordDescribe> describeList = new ArrayList<>();
        try {
            String targetLowCase = target.toLowerCase(Locale.ROOT);
            List<JsonNode> nodeList = DicPreHeat.catalogueMap.get(targetLowCase);
            if (Objects.nonNull(nodeList)) {
                existed = true;
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
            } else {
                existed = false;
            }
        } catch (Exception e) {
            log.error("字典查询异常", e);
            throw new RuntimeException(e);
        }

        Word word = new Word();
        word.setExisted(existed);
        word.setValue(target);
        word.setDescribeList(describeList);
        return word;
    }

    @Override
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
