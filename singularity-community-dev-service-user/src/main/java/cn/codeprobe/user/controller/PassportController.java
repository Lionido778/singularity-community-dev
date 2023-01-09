package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.BaseController;
import cn.codeprobe.api.controller.user.PassportControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.user.service.impl.UserServiceImpl;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Lionido
 */
@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    @Resource
    private UserServiceImpl userService;

    @Override
    public JsonResult getSmsCode(String mobile) {
        // 校验BO数据
        if (CharSequenceUtil.isBlank(mobile)) {
            return JsonResult.errorCustom(ResponseStatusEnum.SMS_MOBILE_BLANK);
        }
        // 调用 service 执行获取验证码操作
        userService.getSMSCode(mobile);
        return JsonResult.ok();
    }

    @Override
    public JsonResult registerLogin(RegisterLoginBO registerLoginBO, BindingResult result) {
        // 校验BO数据
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JsonResult.errorMap(errorMap);
        }
        // 校验验证码
        String redisSmsCode = redisUtil.get(MOBILE_SMS_CODE + ":" + registerLoginBO.getMobile());
        if (CharSequenceUtil.isBlank(redisSmsCode) || !redisSmsCode.equals(registerLoginBO.getSmsCode())) {
            return JsonResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 调用 service 执行注册登陆
        AppUser appUser = userService.RegisterLogin(registerLoginBO);
        return JsonResult.ok(appUser.getActiveStatus());
    }

    @Override
    public JsonResult logout(String userId) {
        userService.userLogout(userId);
        return JsonResult.ok();
    }

}
