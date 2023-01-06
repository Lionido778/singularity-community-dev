package cn.codeprobe.api.controller.base;

import cn.codeprobe.utils.RedisUtil;
import cn.codeprobe.utils.SMSUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这是一个基础 Controller
 * 目的：提供一些常量和属性，方便解耦
 */
public class BaseController {

    @Resource
    public SMSUtil smsUtil;

    @Resource
    public RedisUtil redisUtil;

    public final static String MOBILE_SMSCODE = "mobile:smscode";
    public final static Long MOBILE_SMSCODE_TIMEOUT = (long) (30 * 60);
    public final static Integer MOBILE_SMSCODE_DIGITS = 6;

    /**
     * 获取前端数据校验的错误信息
     *
     * @param result
     * @return
     */
    public Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            map.put(field, message);
        }
        return map;
    }
}
