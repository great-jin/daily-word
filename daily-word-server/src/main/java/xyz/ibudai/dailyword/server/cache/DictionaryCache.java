package xyz.ibudai.dailyword.server.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.props.DictionaryProps;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryCache implements ApplicationRunner {

    private final ObjectMapper objectMapper;

    private final DictionaryProps dictionaryProps;


    public static final Map<String, List<JsonNode>> WORDS = new ConcurrentHashMap<>();

    public static final Map<Catalogue, Map<Integer, TaskWordDTO>> VOCABULARY = new ConcurrentHashMap<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            this.cacheDictionary();
        } catch (Exception e) {
            log.error("Preheat dictionary data failed.", e);
        }

        // 遍历词汇本
        File vocabularyFile = new File(dictionaryProps.getVocabularyPath());
        File[] vocabularyFiles = vocabularyFile.listFiles();
        if (Objects.isNull(vocabularyFiles)) {
            log.error("The directory of [{}] is empty", vocabularyFile.getAbsolutePath());
            return;
        }

        // 分类型缓存词汇
        for (File targetFile : vocabularyFiles) {
            if (targetFile.isDirectory()) {
                continue;
            }

            try {
                // 文件名为类型
                String fileName = targetFile.getName();
                String name = fileName.substring(0, fileName.indexOf("."));
                this.cacheVocabulary(Catalogue.valueOf(name), targetFile);
            } catch (Exception e) {
                log.error("Preheat vocabulary book of [{}] failed.", targetFile.getAbsolutePath(), e);
            }
        }
    }


    /**
     * 读取字典内容并缓存
     */
    private void cacheDictionary() throws Exception {
        Path path = Paths.get(dictionaryProps.getDictionaryPath());
        try (InputStream inputStream = Files.newInputStream(path)) {
            JsonNode node = objectMapper.readTree(inputStream);
            for (int i = 0; i < node.size(); i++) {
                String name = node.get(i)
                        .get("name")
                        .asText();
                name = name.toLowerCase(Locale.ROOT);

                // 词汇中文翻译
                List<JsonNode> nodeList = WORDS.get(name);
                if (CollectionUtils.isEmpty(nodeList)) {
                    nodeList = new ArrayList<>();
                    nodeList.add(node.get(i));
                    WORDS.put(name, nodeList);
                } else {
                    nodeList.add(node.get(i));
                    WORDS.put(name, nodeList);
                }
            }
        }
    }

    /**
     * 读取词汇本内容并缓存
     *
     * @param catalog the catalog
     * @param file    the file
     * @throws Exception the exception
     */
    private void cacheVocabulary(Catalogue catalog, File file) throws Exception {
        Map<Integer, TaskWordDTO> catalogItemMaps = new HashMap<>();

        // 读取文件内容
        JsonNode node = objectMapper.readTree(file);
        for (int i = 0; i < node.size(); i++) {
            JsonNode item = node.get(i);

            // 中文翻译内容
            JsonNode trans = item.get("trans");
            List<String> translationList = new ArrayList<>();
            for (JsonNode tran : trans) {
                translationList.add(tran.asText());
            }

            // 构建单词对象
            TaskWordDTO word = new TaskWordDTO();
            word.setOffset(i);
            word.setCatalogue(catalog);
            word.setValue(item.get("name").asText());
            word.setTranslation(translationList);
            catalogItemMaps.put(i, word);
        }
        VOCABULARY.put(catalog, catalogItemMaps);
    }
}
