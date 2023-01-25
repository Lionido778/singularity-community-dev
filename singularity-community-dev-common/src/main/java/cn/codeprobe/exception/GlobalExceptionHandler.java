package cn.codeprobe.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.result.JsonResult;

/**
 * 全局统一异常处理器 拦截服务内部抛出到controller的指定类型的异常，并以json格式响应给前端 原理：AOP切面
 *
 * @author Lionido
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 拦截 CustomException 自定义异常
     *
     * @param e CustomException
     * @return JSONResult.exception
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public JsonResult returnCustomException(CustomException e) {
        e.printStackTrace();
        return JsonResult.exception(e.getResponseStatusEnum());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult return500(Exception e) {
        if (e instanceof CustomException) {
            returnCustomException((CustomException)e);
        }
        e.printStackTrace();
        return JsonResult.exception(ResponseStatusEnum.SYSTEM_INTERNAL_ERROR);
    }

    /**
     * 拦截文件上传大小限制异常
     *
     * @return JSONResult.exception
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public JsonResult returnSizeLimitExceededException() {
        return JsonResult.exception(ResponseStatusEnum.FILE_MAX_SIZE_ERROR);
    }

}
