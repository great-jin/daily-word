package xyz.ibudai.dailyword.model.config;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class SystemConfig {

    private static final Integer SEASON;

    static {
        // 读取月份计算赛季
        YearMonth targetMonth = YearMonth.of(2025, 3);
        SEASON = Math.toIntExact(ChronoUnit.MONTHS.between(YearMonth.now(), targetMonth)) + 1;
    }


    public static Integer getSeason() {
        return SEASON;
    }

    public static Integer getUndoneLimit() {
        return 5;
    }
}
