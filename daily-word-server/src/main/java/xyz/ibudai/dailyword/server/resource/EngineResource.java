package xyz.ibudai.dailyword.server.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.engine.model.RequestDTO;
import xyz.ibudai.dailyword.engine.model.ResponseDTO;
import xyz.ibudai.dailyword.engine.EngineClient;
import xyz.ibudai.dailyword.model.base.ResponseData;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/server/engine")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EngineResource {

    private final EngineClient engineClient;


    /**
     * Translate data.
     *
     * @param reqDto the req dto
     * @return the response data
     */
    @PostMapping("translate")
    public ResponseData translate(@RequestBody RequestDTO reqDto) {
        try {
            // 校验数据
            if (Objects.isNull(reqDto)) {
                return ResponseData.failed("内容为空！");
            }
            String text = reqDto.getText();
            if (StringUtils.isBlank(text)) {
                return ResponseData.failed("内容为空！");
            }
            if (text.length() > 250) {
                return ResponseData.failed("内容不可超过 250 字符！");
            }

            // 翻译内容
            ResponseDTO res = engineClient.translate(reqDto);
            if (Objects.isNull(res)) {
                log.error("Request engine null");
                return ResponseData.failed("系统繁忙！");
            }

            return ResponseData.success(res.getTargetText());
        } catch (Exception e) {
            log.error("Engine translate error", e);
            return ResponseData.failed("系统繁忙！");
        }
    }
}
