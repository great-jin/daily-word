package xyz.ibudai.dailyword.server.cache;

import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.enums.Catalogue;

import java.util.*;
import java.util.stream.Collectors;

public class DictTool {

    /**
     * Extract list.
     *
     * @param catalogue  the catalogue
     * @param wordIndies the word indies
     * @return the list
     */
    public static List<TaskWordDTO> extract(Catalogue catalogue, String wordIndies) {
        Set<Integer> offsets = Arrays.stream(wordIndies.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
        return extract(catalogue, offsets);
    }


    /**
     * Extract list.
     *
     * @param catalogue the catalogue
     * @param offsets   the offsets
     * @return the list
     */
    public static List<TaskWordDTO> extract(Catalogue catalogue, Set<Integer> offsets) {
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
