package xyz.ibudai.dailyword.engine.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.engine.enums.DataType;
import xyz.ibudai.dailyword.engine.model.RequestDTO;
import xyz.ibudai.dailyword.engine.model.ResponseDTO;
import xyz.ibudai.dailyword.model.props.EngineProps;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EngineClient {

    private final EngineProps engineProps;

    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;


    /**
     * Translate response dto.
     *
     * @param reqDto the req dto
     * @return the response dto
     * @throws JsonProcessingException the json processing exception
     */
    public ResponseDTO translate(RequestDTO reqDto) throws JsonProcessingException {
        if (Objects.isNull(reqDto)) {
            return null;
        }

        // 构建请求
        String data = objectMapper.writeValueAsString(reqDto);
        MediaType mediaType = MediaType.parse(DataType.JSON.getType());
        Request request = new Request.Builder()
                .url(engineProps.getUrl())
                .post(RequestBody.create(data, mediaType))
                .build();

        // 请求引擎服务
        String result = callRequest(request);
        if (StringUtils.isBlank(result)) {
            return null;
        }

        return objectMapper.readValue(result, ResponseDTO.class);
    }


    /**
     * Call request string.
     *
     * @param request the request
     * @return the string
     */
    private String callRequest(Request request) {
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
