package cn.codeprobe.api.controller.user;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户通行证接口
 *
 * @author Lionido
 */
@Api(value = "一键注册登录", tags = "一键注册登录接口")
@RequestMapping("/passport")
public interface UserPassportControllerApi {

    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     * @return sms_code(6位)
     */
    @ApiOperation(value = "获取短信验证码", notes = "获取短信验证码60秒", httpMethod = "GET")
    @GetMapping("/querySMSCode")
    JsonResult querySmsCode(@RequestParam String mobile);

    /**
     * 一键注册登录
     *
     * @param registerLoginBO 注册登陆表单
     * @return yes / no
     */
    @ApiOperation(value = "用户一键注册登录接口", notes = "用户一键注册登录", httpMethod = "POST")
    @PostMapping("/doLogin")
    JsonResult registerLogin(@RequestBody @Valid RegisterLoginBO registerLoginBO);

    /**
     * 用户退出登录
     *
     * @param userId 用户ID
     * @return yes / no
     */
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    JsonResult logout(@RequestParam String userId);

}
