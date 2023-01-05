package cn.codeprobe.exception;

import cn.codeprobe.result.JSONResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局统一异常处理
 * 拦截服务内部抛出到 controller 的指定类型的异常，并以json格式响应给前端
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 拦截 CustomException 自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public JSONResult returnCustomException(CustomException e) {
        e.printStackTrace();
        return JSONResult.exception(e.getResponseStatusEnum());
    }

}
