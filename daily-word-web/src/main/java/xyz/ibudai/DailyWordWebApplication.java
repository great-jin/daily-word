package xyz.ibudai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
        scanBasePackages = "xyz.ibudai"
//        exclude = {SecurityAutoConfiguration.class}
)
@MapperScan("xyz.ibudai.dao")
public class DailyWordWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyWordWebApplication.class, args);
    }

}
