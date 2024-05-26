package xyz.ibudai.dailyword.handle;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.ibudai.dailyword.common.ResponseData;

@RestControllerAdvice
public class ExceptionHandle {

    /**
     * 监听异常请求并处理返回
     */
    @ExceptionHandler(Exception.class)
    public ResponseData handleNotFoundException(Exception ex) {
        return ResponseData.failed(ex.getMessage());
    }
}
