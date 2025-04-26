package xyz.ibudai.dailyword.engine.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestClient {

    private final OkHttpClient okHttpClient;


    /**
     * Call request string.
     *
     * @param request the request
     * @return the string
     */
    public String callRequest(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // 响应失败
                throw new IllegalStateException("Unexpected code: " + response.code());
            }

            // 获取响应体内容
            ResponseBody body = response.body();
            if (body == null) {
                throw new NullPointerException("Response body is null");
            }
            return body.string();
        } catch (Exception e) {
            log.error("Call engine server error", e);
            return null;
        }
    }
}
