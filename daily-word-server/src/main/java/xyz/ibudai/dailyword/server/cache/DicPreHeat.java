package xyz.ibudai.dailyword.server.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.enums.Catalogue;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DicPreHeat implements ApplicationRunner {

    @Value("${resource.dict}")
    private String dictPath;

    @Value("${resource.vocabulary}")
    private String vocabularyPath;


    @Autowired
    private ObjectMapper objectMapper;

    public static final Map<String, List<JsonNode>> CATALOG_MAP = new ConcurrentHashMap<>();

    public static final Map<Catalogue, Map<Integer, TaskWordDTO>> DICT_CACHE = new ConcurrentHashMap<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            this.preheatDict();
        } catch (Exception e) {
            log.error("Preheat dictionary data failed.", e);
        }

        File file = new File(vocabularyPath);
        File[] vocabularyFiles = file.listFiles();
        if (Objects.isNull(vocabularyFiles)) {
            log.error("The directory of [{}] is empty", vocabularyPath);
            return;
        }

        for (File vocabularyFile : vocabularyFiles) {
            if (vocabularyFile.isDirectory()) {
                continue;
            }

            try {
                String fileName = vocabularyFile.getName();
                String name = fileName.substring(0, fileName.indexOf("."));
                Catalogue catalogue = Catalogue.valueOf(name);
                preheatData(catalogue, vocabularyFile);
            } catch (Exception e) {
                log.error("Preheat vocabulary book of [{}] failed.", vocabularyFile.getAbsolutePath(), e);
            }
        }
    }

    private void preheatDict() throws Exception {
        try (InputStream inputStream = Files.newInputStream(Paths.get(dictPath));) {
            JsonNode node = objectMapper.readTree(inputStream);
            for (int i = 0; i < node.size(); i++) {
                String name = node.get(i)
                        .get("name")
                        .asText();
                name = name.toLowerCase(Locale.ROOT);
                List<JsonNode> nodeList = CATALOG_MAP.get(name);
                if (Objects.isNull(nodeList) || nodeList.isEmpty()) {
                    nodeList = new ArrayList<>();
                    nodeList.add(node.get(i));
                    CATALOG_MAP.put(name, nodeList);
                } else {
                    nodeList.add(node.get(i));
                    CATALOG_MAP.put(name, nodeList);
                }
            }
        }
    }

    private void preheatData(Catalogue type, File file) throws Exception {
        Map<Integer, TaskWordDTO> cache = new HashMap<>();
        JsonNode node = objectMapper.readTree(file);
        for (int i = 0; i < node.size(); i++) {
            JsonNode item = node.get(i);
            TaskWordDTO word = new TaskWordDTO();
            String name = item.get("name")
                    .asText();
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
        DICT_CACHE.put(type, cache);
    }
}
