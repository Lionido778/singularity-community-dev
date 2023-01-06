package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.BaseController;
import cn.codeprobe.api.controller.user.PassportControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.result.JSONResult;
import cn.codeprobe.user.service.impl.UserServiceImpl;
import cn.codeprobe.utils.IPUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    @Resource
    private UserServiceImpl userService;

    @Override
    public JSONResult getSMSCode(String mobile, HttpServletRequest request) throws Exception {
        // 校验BO数据
        if (StrUtil.isBlank(mobile)) {
            return JSONResult.errorCustom(ResponseStatusEnum.SMS_MOBILE_BLANK);
        }
        // 获取用户ip，限制用户只允许在60秒内发送一次短信
        String requestIp = IPUtil.getRequestIp(request);
        redisUtil.setnx60s(MOBILE_SMSCODE + ":" + requestIp, requestIp);
        // 生成六位数随机数字验证码，并发送
        String randomCode = RandomUtil.randomNumbers(MOBILE_SMSCODE_DIGITS);
//        smsUtil.sendSms(mobile, randomCode);
        // 将验证码存入redis，有效期30分钟，方便后续验证
        redisUtil.set(MOBILE_SMSCODE + ":" + mobile, randomCode, MOBILE_SMSCODE_TIMEOUT);
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
        String redisSmsCode = redisUtil.get(MOBILE_SMSCODE + ":" + registerLoginBO.getMobile());
        if (StrUtil.isBlank(redisSmsCode) || !redisSmsCode.equals(registerLoginBO.getSmsCode())) {
            return JSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 调用 service 执行登陆操作
        AppUser appUser = userService.RegisterLogin(registerLoginBO);
        return JSONResult.ok(appUser);
    }

}
