package xyz.ibudai.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DicPreHeat implements ApplicationRunner {

    @Value("${dictionary.path}")
    private String dictPath;

    @Autowired
    private ObjectMapper objectMapper;

    public static final Map<String, List<JsonNode>> catalogueMap = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("预热字典缓存数据.");
        File file = new File(dictPath);
        if (!file.exists() || !file.isFile()) {
            log.error("字段文件不存在，请检查配置。");
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
}
