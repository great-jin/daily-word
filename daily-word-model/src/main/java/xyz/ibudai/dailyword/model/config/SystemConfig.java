package xyz.ibudai.dailyword.model.config;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class SystemConfig {

    private static final Integer SEASON;

    static {
        // 读取月份计算赛季
        YearMonth targetMonth = YearMonth.of(2025, 3);
        SEASON = -1 * Math.toIntExact(ChronoUnit.MONTHS.between(YearMonth.now(), targetMonth)) + 1;
    }


    public static Integer getSeason() {
        return SEASON;
    }

    public static Integer getUndoneLimit() {
        return 5;
    }

    public static LocalDateTime getMonthLastHour() {
        LocalDateTime now = LocalDateTime.now();
        int lastDay = YearMonth.from(now).lengthOfMonth();
        // 当月最后一天的 23:00:00
        return LocalDateTime.of(
                now.getYear(),
                now.getMonth(),
                lastDay,
                23,
                0,
                0);
    }
}
