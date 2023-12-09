package xyz.ibudai.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import xyz.ibudai.model.TaskWord;
import xyz.ibudai.model.common.Catalogue;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DicPreHeat implements ApplicationRunner {

    @Value("${dictionary.path.dict}")
    private String dictPath;
    @Value("${dictionary.path.cet4}")
    private String cet4Path;

    @Autowired
    private ObjectMapper objectMapper;

    public static final Map<String, List<JsonNode>> catalogueMap = new ConcurrentHashMap<>();

    public static final Map<String, TaskWord> cet4Cache = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            this.preheatDict();
        } catch (Exception e) {
            log.error("预热字典缓存数据异常");
        }

        try {
            this.preheatCET4();
        } catch (Exception e) {
            log.error("预热 CET4 缓存数据异常");
        }
    }

    private void preheatDict() throws Exception {
        File file = new File(dictPath);
        if (!file.exists() || !file.isFile()) {
            log.error("字典文件不存在，请检查配置。");
            return;
        }

        JsonNode node = objectMapper.readTree(file);
        for (int i = 0; i < node.size(); i++) {
            String name = node.get(i).get("name").asText();
            name = name.toLowerCase(Locale.ROOT);
            List<JsonNode> nodeList = catalogueMap.get(name);
            if (Objects.isNull(nodeList) || nodeList.isEmpty()) {
                nodeList = new ArrayList<>();
                nodeList.add(node.get(i));
                catalogueMap.put(name, nodeList);
            } else {
                nodeList.add(node.get(i));
                catalogueMap.put(name, nodeList);
            }
        }
    }

    private void preheatCET4() throws Exception {
        File file = new File(cet4Path);
        if (!file.exists() || !file.isFile()) {
            log.error("CET4 文件不存在，请检查配置。");
            return;
        }

        JsonNode node = objectMapper.readTree(file);
        for (int i = 0; i < node.size(); i++) {
            JsonNode item = node.get(i);
            TaskWord word = new TaskWord();
            String name = item.get("name").asText();
            word.setValue(name);
            word.setOffset(i);
            JsonNode trans = item.get("trans");
            List<String> translationList = new ArrayList<>();
            for (JsonNode tran : trans) {
                translationList.add(tran.asText());
            }
            word.setTranslation(translationList);
            word.setCatalogue(Catalogue.CET4);
            cet4Cache.put(name, word);
        }
    }
}
