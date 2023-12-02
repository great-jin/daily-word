package xyz.ibudai.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import xyz.ibudai.cache.DicPreHeat;
import xyz.ibudai.model.Word;
import xyz.ibudai.model.WordDescribe;
import xyz.ibudai.service.WordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WordServiceImpl implements WordService {

    @Override
    public Word translation(String target) {
        Word word = new Word();
        word.setValue(target);
        try {
            JsonNode node = DicPreHeat.catalogueMap.get(target);
            if (Objects.nonNull(node)) {
                String name = node.get("name").asText();
                if (name.equals(target)) {
                    List<WordDescribe> describeList = new ArrayList<>();
                    for (JsonNode jsonNode : node.get("describerList")) {
                        String type = jsonNode.get("type").asText();
                        String describe = jsonNode.get("describe").toString();
                        describe = describe.replace("\"", "");
                        describeList.add(new WordDescribe(type, describe));
                    }
                    word.setDescribeList(describeList);
                }
            } else {
                word.setDescribeList(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return word;
    }
}
