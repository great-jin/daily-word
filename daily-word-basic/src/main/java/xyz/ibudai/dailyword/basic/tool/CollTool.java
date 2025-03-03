package xyz.ibudai.dailyword.basic.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The type Coll tool.
 */
public class CollTool {

    /**
     * Partition list.
     *
     * @param <T>       the type parameter
     * @param list      the list
     * @param batchSize the batch size
     * @return the list
     */
    public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return Collections.emptyList();
        }

        int start = 0;
        int size = list.size();
        int end = Math.min(size, batchSize);
        List<List<T>> result = new ArrayList<>();
        while (start <= end) {
            if (start == end) {
                break;
            }

            result.add(list.subList(start, end));
            start = end;
            end = size - end < batchSize ? size : end + batchSize;
        }
        return result;
    }
}
