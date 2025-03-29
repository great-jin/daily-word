package xyz.ibudai.dailyword.model.config;

public class SystemConfig {

    private static final Integer SEASON = 1;

    static {
        // TODO 2025/3/29 读取月份计算赛季

    }


    public static Integer getSeason() {
        return SEASON;
    }
}
