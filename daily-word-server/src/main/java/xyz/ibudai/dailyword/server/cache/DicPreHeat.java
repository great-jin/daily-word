package xyz.ibudai.dailyword.server.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.repository.model.TaskWord;
import xyz.ibudai.dailyword.repository.enums.Catalogue;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DicPreHeat implements ApplicationRunner {

    @Value("classpath:dict/Dictionary.json")
    private Resource dictResource;

    @Value("classpath:vocabulary")
    private Resource vocabularyResource;

    @Autowired
    private ObjectMapper objectMapper;

    public static final Map<String, List<JsonNode>> catalogueMap = new ConcurrentHashMap<>();

    public static final Map<Catalogue, Map<Integer, TaskWord>> dictCache = new ConcurrentHashMap<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            this.preheatDict(dictResource);
        } catch (Exception e) {
            log.error("Preheat dictionary data failed.", e);
        }

        if (!vocabularyResource.exists()) {
            log.error("The directory [{}] is not exists", vocabularyResource.getFilename());
            return;
        }
        File file = new File(vocabularyResource.getURI());
        File[] vocabularyFiles = file.listFiles();
        if (Objects.isNull(vocabularyFiles)) {
            log.error("The directory of [{}] is empty", vocabularyResource.getFilename());
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

    private void preheatDict(Resource resource) throws Exception {
        JsonNode node = objectMapper.readTree(resource.getInputStream());
        for (int i = 0; i < node.size(); i++) {
            String name = node.get(i)
                    .get("name")
                    .asText();
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

    private void preheatData(Catalogue type, File file) throws Exception {
        Map<Integer, TaskWord> cache = new HashMap<>();
        JsonNode node = objectMapper.readTree(file);
        for (int i = 0; i < node.size(); i++) {
            JsonNode item = node.get(i);
            TaskWord word = new TaskWord();
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
        dictCache.put(type, cache);
    }
}
