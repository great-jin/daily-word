package xyz.ibudai.dailyword.server.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.ibudai.dailyword.basic.tool.CollTool;
import xyz.ibudai.dailyword.model.dto.RoomDTO;
import xyz.ibudai.dailyword.model.entity.MatchDetail;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.vo.DictDetail;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.vo.match.MatchVo;
import xyz.ibudai.dailyword.model.vo.word.Word;
import xyz.ibudai.dailyword.model.vo.word.WordDescribe;
import xyz.ibudai.dailyword.repository.service.MatchDetailService;
import xyz.ibudai.dailyword.repository.service.MatchRecordService;
import xyz.ibudai.dailyword.repository.util.SecurityUtil;
import xyz.ibudai.dailyword.server.cache.DicPreHeat;
import xyz.ibudai.dailyword.server.service.WordService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WordServiceImpl implements WordService {

    private final MatchDetailService matchDetailService;
    private final MatchRecordService matchRecordService;


    public List<DictDetail> getDictDetail() {
        List<DictDetail> detailList = new ArrayList<>();
        for (Map.Entry<Catalogue, Map<Integer, TaskWordDTO>> Catalogue : DicPreHeat.DICT_CACHE.entrySet()) {
            Catalogue catalogue = Catalogue.getKey();
            int wordCount = Catalogue.getValue().size();
            DictDetail detail = new DictDetail(catalogue, wordCount);
            detailList.add(detail);
        }
        return detailList;
    }

    public Word translation(String target) {
        String targetLowCase = target.toLowerCase(Locale.ROOT);
        List<JsonNode> nodeList = DicPreHeat.CATALOG_MAP.get(targetLowCase);
        if (Objects.isNull(nodeList)) {
            return Word.notFound(target);
        }

        try {
            List<WordDescribe> describeList = new ArrayList<>();
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

            Word word = new Word();
            word.setExisted(true);
            word.setValue(target);
            word.setDescribeList(describeList);
            return word;
        } catch (Exception e) {
            log.error("字典查询异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public MatchVo starTask(Catalogue catalogue, Integer size) {
        Set<Integer> users = Set.of(SecurityUtil.getLoginUser());
        // 房间信息
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomSize(0);
        roomDTO.setCatalogue(catalogue);
        roomDTO.setSize(size);

        List<TaskWordDTO> dataList = this.getTaskContent(catalogue, size);

        // 初始化记录
        List<Integer> offsets = dataList.stream().map(TaskWordDTO::getOffset).toList();
        roomDTO.setWordIndies(StringUtils.join(offsets, ","));
        Integer matchId = matchRecordService.initRecord(users, roomDTO);

        return new MatchVo(matchId, dataList);
    }

    public List<TaskWordDTO> getTaskContent(Catalogue catalogue, Integer size) {
        if (Objects.isNull(size) || size <= 0) {
            size = 20;
        }

        Collection<TaskWordDTO> collection = DicPreHeat.DICT_CACHE
                .get(catalogue)
                .values();
        return extract(catalogue, CollTool.randoms(collection, size));
    }

    @Override
    public List<TaskWordDTO> getAnswer(String matchId) {
        MatchDetail matchDetail = matchDetailService.getById(matchId);
        if (Objects.isNull(matchDetail)) {
            return Collections.emptyList();
        }

        String wordIndies = matchDetail.getWordIndies();
        Set<Integer> offsets = Arrays.stream(wordIndies.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
        Catalogue catalogue = Catalogue.valueOf(matchDetail.getCatalog());
        return extract(catalogue, offsets);
    }


    private List<TaskWordDTO> extract(Catalogue catalogue, Set<Integer> offsets) {
        Collection<TaskWordDTO> collection = DicPreHeat.DICT_CACHE
                .get(catalogue)
                .values();
        List<TaskWordDTO> taskList = new ArrayList<>();
        for (TaskWordDTO item : collection) {
            if (Objects.equals(taskList.size(), offsets.size())) {
                break;
            }
            if (offsets.contains(item.getOffset())) {
                taskList.add(item);
            }
        }
        return taskList;
    }
}
