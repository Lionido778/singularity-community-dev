package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.BaseController;
import cn.codeprobe.api.controller.user.PassportControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.result.JSONResult;
import cn.codeprobe.user.service.impl.UserServiceImpl;
import cn.hutool.core.util.StrUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    @Resource
    private UserServiceImpl userService;

    @Override
    public JSONResult getSMSCode(String mobile) throws Exception {
        // 校验BO数据
        if (StrUtil.isBlank(mobile)) {
            return JSONResult.errorCustom(ResponseStatusEnum.SMS_MOBILE_BLANK);
        }
        // 调用 service 执行获取验证码操作
        userService.getSMSCode(mobile);
        return JSONResult.ok();
    }

    @Override
    public JSONResult RegisterLogin(RegisterLoginBO registerLoginBO, BindingResult result) {
        // 校验BO数据
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JSONResult.errorMap(errorMap);
        }
        // 校验验证码
        String redisSmsCode = redisUtil.get(MOBILE_SMS_CODE + ":" + registerLoginBO.getMobile());
        if (StrUtil.isBlank(redisSmsCode) || !redisSmsCode.equals(registerLoginBO.getSmsCode())) {
            return JSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 调用 service 执行注册登陆
        AppUser appUser = userService.RegisterLogin(registerLoginBO);
        return JSONResult.ok(appUser.getActiveStatus());
    }

}
