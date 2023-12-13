package xyz.ibudai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@MapperScan("xyz.ibudai.dao")
@SpringBootApplication(scanBasePackages = "xyz.ibudai")
public class DailyWordWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyWordWebApplication.class, args);
    }

}
