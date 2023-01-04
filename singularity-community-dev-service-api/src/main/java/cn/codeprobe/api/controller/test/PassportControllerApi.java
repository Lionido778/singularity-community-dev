package cn.codeprobe.api.controller.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "短信发送测试接口", tags = "阿里云sms短信测试接口")
public interface PassportControllerApi {

    @ApiOperation(value = "短信发送测试接口", notes = "短信发送测试接口", httpMethod = "GET")
    @GetMapping("/sendSms")
    Object sendSms() throws Exception;
}
