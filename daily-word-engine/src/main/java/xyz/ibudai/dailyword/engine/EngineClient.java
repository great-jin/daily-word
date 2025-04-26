package xyz.ibudai.dailyword.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.engine.enums.Model;
import xyz.ibudai.dailyword.engine.model.RequestDTO;
import xyz.ibudai.dailyword.engine.model.ResponseDTO;
import xyz.ibudai.dailyword.engine.translator.AbstractTranslator;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EngineClient {

    private final Map<String, AbstractTranslator> translators;


    protected AbstractTranslator getTranslator(Model model) {
        return switch (model) {
            case NLLB -> translators.get("nllbTranslator");
            case OPUS -> translators.get("opusTranslator");
        };
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

        // 获取实例
        AbstractTranslator translator = getTranslator(reqDto.getModel());

        // 请求服务
        return translator.translate(reqDto);
    }
}
