package xyz.ibudai.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
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

    @Autowired
    private ObjectMapper objectMapper;

    public static final Map<String, List<JsonNode>> catalogueMap = new ConcurrentHashMap<>();

    public static final Map<Integer, TaskWord> cet4Cache = new ConcurrentHashMap<>();

    public static final Map<Integer, TaskWord> cet6Cache = new ConcurrentHashMap<>();

    public static final Map<Integer, TaskWord> greCache = new ConcurrentHashMap<>();

    public static final Map<Integer, TaskWord> graduateCache = new ConcurrentHashMap<>();

    public static final Map<Integer, TaskWord> oxfordCache = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            String path = "dict\\Dictionary.json";
            this.preheatDict(path);
        } catch (Exception e) {
            log.error("预热字典缓存数据异常", e);
        }

        try {
            String path = "dict\\CET4.json";
            this.preheatData(Catalogue.CET4, path, cet4Cache);
        } catch (Exception e) {
            log.error("预热 CET4 缓存数据异常", e);
        }

        try {
            String path = "dict\\CET6.json";
            this.preheatData(Catalogue.CET6, path, cet6Cache);
        } catch (Exception e) {
            log.error("预热 CET6 缓存数据异常", e);
        }

        try {
            String path = "dict\\GRE.json";
            this.preheatData(Catalogue.GRE, path, greCache);
        } catch (Exception e) {
            log.error("预热 GRE 缓存数据异常", e);
        }

        try {
            String path = "dict\\Graduate.json";
            this.preheatData(Catalogue.Graduate, path, graduateCache);
        } catch (Exception e) {
            log.error("预热 Graduate 缓存数据异常", e);
        }

        try {
            String path = "dict\\Oxford.json";
            this.preheatData(Catalogue.Oxford, path, oxfordCache);
        } catch (Exception e) {
            log.error("预热 Oxford 缓存数据异常", e);
        }
    }

    private void preheatDict(String path) throws Exception {
        File file = new ClassPathResource(path).getFile();
        if (!file.exists() || !file.isFile()) {
            log.error("字典 {} 文件不存在，请检查配置。", path);
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

    private void preheatData(Catalogue type, String path, Map<Integer, TaskWord> cache) throws Exception {
        File file = new ClassPathResource(path).getFile();
        if (!file.exists() || !file.isFile()) {
            log.error("预热数据文件 {} 不存在，请检查文件路径。", path);
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
            word.setCatalogue(type);
            cache.put(i, word);
        }
    }
}
