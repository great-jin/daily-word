package xyz.ibudai.dailyword.engine.config;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.ibudai.dailyword.model.props.EngineProps;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HttpConfig {

    private final EngineProps engineProps;


    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                // 连接超时时间
                .connectTimeout(engineProps.getConnTimeout(), TimeUnit.MINUTES)
                // 请求超时时间
                .readTimeout(engineProps.getReadTimeout(), TimeUnit.MINUTES)
                .build();
    }
}
