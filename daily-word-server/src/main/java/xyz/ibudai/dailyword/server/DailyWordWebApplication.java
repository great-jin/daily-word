package xyz.ibudai.dailyword.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = {
        "xyz.ibudai.dailyword",
        "xyz.ibudai.dailyword.auth",
        "xyz.ibudai.dailyword.repository",
        "xyz.ibudai.dailyword.server"
})
@MapperScan("xyz.ibudai.dailyword.repository.dao")
public class DailyWordWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyWordWebApplication.class, args);
    }

}
