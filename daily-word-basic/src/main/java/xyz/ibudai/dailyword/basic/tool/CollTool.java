package xyz.ibudai.dailyword.basic.tool;

import java.util.*;

/**
 * The type Coll tool.
 */
public class CollTool {

    public static <T> Set<Integer> randoms(Collection<T> collection, int batchSize) {
        Set<Integer> indies = new HashSet<>(batchSize);
        Random random = new Random();
        while (indies.size() < batchSize) {
            indies.add(random.nextInt(collection.size()));
        }
        return indies;
    }

    /**
     * Find batch list.
     *
     * @param <T>        the type parameter
     * @param collection the list
     * @param batchSize  the batch size
     * @return the list
     */
    public static <T> List<T> findBatch(Collection<T> collection, int batchSize) {
        List<T> list = new ArrayList<>(collection);
        Set<Integer> indies = new HashSet<>(batchSize);
        Random random = new Random();
        while (indies.size() < batchSize) {
            indies.add(random.nextInt(list.size()));
        }
        return indies.stream()
                .map(list::get)
                .toList();
    }

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
