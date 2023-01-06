package cn.codeprobe.api.controller.user;

import cn.codeprobe.result.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "用户信息相关接口", tags = "用户信息相关接口")
@RequestMapping("/user")
public interface UserControllerApi {

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    JSONResult getUserInfo(@RequestParam String userId);

}
