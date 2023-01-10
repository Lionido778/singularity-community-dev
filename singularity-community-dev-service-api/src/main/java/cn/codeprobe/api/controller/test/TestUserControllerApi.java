package cn.codeprobe.api.controller.test;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Lionido
 */
@Api(value = "项目测试接口", tags = "项目测试接口")
@RequestMapping("/user/test")
public interface TestUserControllerApi {

    @ApiOperation(value = "用户项目测试Hello接口", notes = "用户项目测试Hello接口", httpMethod = "GET")
    @GetMapping("/hello")
    Object hello();

    @ApiOperation(value = "短信发送测试接口", notes = "短信发送测试接口", httpMethod = "GET")
    @GetMapping("/sendSms")
    Object sendSms() throws Exception;

    @ApiOperation(value = "Redis测试接口", notes = "测试Redis是否整合成功", httpMethod = "GET")
    @GetMapping("/redis")
    JsonResult redis();
}
