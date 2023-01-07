package cn.codeprobe.api.controller.user;

import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.result.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "一键注册登录", tags = "一键注册登录接口")
@RequestMapping("/passport")
public interface PassportControllerApi {

    @ApiOperation(value = "获取短信验证码", notes = "获取短信验证码60秒", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    JSONResult getSMSCode(@RequestParam String mobile) throws Exception;

    @ApiOperation(value = "一键注册登录", notes = "用户一键注册登录接口", httpMethod = "POST")
    @PostMapping("/doLogin")
    JSONResult RegisterLogin(@RequestBody @Valid RegisterLoginBO registerLoginBO, BindingResult result);

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    JSONResult logout(@RequestParam String userId);
}
