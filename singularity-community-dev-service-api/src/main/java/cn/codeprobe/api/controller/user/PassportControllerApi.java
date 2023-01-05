package cn.codeprobe.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Api(value = "用户注册登录接口", tags = "用户注册登录接口")
@RequestMapping("/passport")
public interface PassportControllerApi {

    @ApiOperation(value = "获取短信验证码", notes = "获取短信验证码60秒", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    Object getSMSCode(String mobile, HttpServletRequest request) throws Exception;

}
