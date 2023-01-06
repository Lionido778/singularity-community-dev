package cn.codeprobe.api.controller.user;

import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.result.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Api(value = "用户信息相关接口", tags = "用户信息相关接口")
@RequestMapping("/user")
public interface UserControllerApi {

    @ApiOperation(value = "获取用户基本信息", notes = "用户基本信息", httpMethod = "POST")
    @PostMapping("/getUserInfo")
    JSONResult getUserBasicInfo(@RequestParam String userId);

    @ApiOperation(value = "获取用户账户信息", notes = "用户账户信息", httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    JSONResult getUserAccountInfo(@RequestParam String userId);

    @ApiOperation(value = "更新用户账户信息", notes = "更新用户账户信息", httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    JSONResult updateUserAccountInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result);

}
