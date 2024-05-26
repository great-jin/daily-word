package xyz.ibudai.dailyword;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@MapperScan("xyz.ibudai.dailyword.dao")
@SpringBootApplication(scanBasePackages = "xyz.ibudai.dailyword")
@EnableConfigurationProperties
public class DailyWordWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyWordWebApplication.class, args);
    }

}
