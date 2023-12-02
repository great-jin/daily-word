package xyz.ibudai.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DicPreHeat implements ApplicationRunner {

    @Autowired
    private ObjectMapper objectMapper;

    public static final Map<String, JsonNode> catalogueMap = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String outputPath = "E:\\Temporary\\word\\dic.json";
        JsonNode node = objectMapper.readTree(new File(outputPath));
        for (int i = 0; i < node.size(); i++) {
            catalogueMap.put(node.get(i).get("name").asText(), node.get(i));
        }
    }
}
