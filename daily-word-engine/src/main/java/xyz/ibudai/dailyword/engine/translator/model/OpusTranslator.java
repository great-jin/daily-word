package xyz.ibudai.dailyword.engine.translator.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import xyz.ibudai.dailyword.engine.request.RequestClient;
import xyz.ibudai.dailyword.engine.translator.AbstractTranslator;
import xyz.ibudai.dailyword.model.props.EngineProps;

@Component("opusTranslator")
public class OpusTranslator extends AbstractTranslator {

    public OpusTranslator(EngineProps engineProps,
                          ObjectMapper objectMapper,
                          RequestClient requestClient) {
        super(engineProps, objectMapper, requestClient);
    }


    @Override
    public String getApi() {
        return engineProps.getOpus();
    }
}
