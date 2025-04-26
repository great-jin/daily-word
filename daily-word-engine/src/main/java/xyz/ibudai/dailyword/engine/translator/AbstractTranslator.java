package xyz.ibudai.dailyword.engine.translator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.engine.enums.DataType;
import xyz.ibudai.dailyword.engine.model.RequestDTO;
import xyz.ibudai.dailyword.engine.model.ResponseDTO;
import xyz.ibudai.dailyword.engine.request.RequestClient;
import xyz.ibudai.dailyword.model.props.EngineProps;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public abstract class AbstractTranslator {

    protected final EngineProps engineProps;

    private final ObjectMapper objectMapper;
    private final RequestClient requestClient;


    public abstract String getApi();


    /**
     * Create req request.
     *
     * @param reqDto the req dto
     * @return the request
     * @throws Exception the exception
     */
    protected Request createReq(RequestDTO reqDto) throws Exception {
        String data = objectMapper.writeValueAsString(reqDto);
        MediaType mediaType = MediaType.parse(DataType.JSON.getType());
        return new Request.Builder()
                .url(this.getApi())
                .post(RequestBody.create(data, mediaType))
                .build();
    }

    /**
     * Handle response response dto.
     *
     * @param result the result
     * @return the response dto
     * @throws Exception the exception
     */
    protected ResponseDTO handleResponse(String result) throws Exception {
        if (StringUtils.isBlank(result)) {
            return null;
        }

        return objectMapper.readValue(result, ResponseDTO.class);
    }


    /**
     * Translate response dto.
     *
     * @param reqDto the req dto
     * @return the response dto
     * @throws JsonProcessingException the json processing exception
     */
    public ResponseDTO translate(RequestDTO reqDto) throws Exception {
        if (Objects.isNull(reqDto)) {
            return null;
        }

        // 构建请求
        Request request = this.createReq(reqDto);

        // 请求引擎服务
        String result = requestClient.callRequest(request);

        // 解析内容
        return this.handleResponse(result);
    }
}
