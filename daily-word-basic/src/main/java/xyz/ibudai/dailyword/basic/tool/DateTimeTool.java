package xyz.ibudai.dailyword.basic.tool;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * The type Date time tool.
 */
public class DateTimeTool {

    private static final Long HOUR_IN_SECOND = TimeUnit.HOURS.toSeconds(1);

    private static final Long DAY_IN_SECOND = TimeUnit.DAYS.toSeconds(1);


    /**
     * Interval string.
     *
     * @param start the start
     * @param end   the end
     * @return the string
     */
    public static String interval(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return "";
        }

        long seconds = Duration.between(start, end).getSeconds();
        if (seconds < HOUR_IN_SECOND) {
            return "刚刚";
        } else if (seconds < DAY_IN_SECOND) {
            long hours = seconds / HOUR_IN_SECOND;
            return hours + " 小时前";
        } else {
            long days = seconds / DAY_IN_SECOND;
            return days + " 天前";
        }
    }
}
