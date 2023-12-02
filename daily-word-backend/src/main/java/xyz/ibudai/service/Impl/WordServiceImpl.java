package xyz.ibudai.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.ibudai.cache.DicPreHeat;
import xyz.ibudai.model.Word;
import xyz.ibudai.model.WordDescribe;
import xyz.ibudai.service.WordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class WordServiceImpl implements WordService {

    @Override
    public Word translation(String target) {
        boolean existed;
        List<WordDescribe> describeList = new ArrayList<>();
        try {
            JsonNode node = DicPreHeat.catalogueMap.get(target);
            if (Objects.nonNull(node)) {
                existed = true;
                String name = node.get("name").asText();
                if (name.equals(target)) {
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
}
