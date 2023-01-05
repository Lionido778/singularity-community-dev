package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.BaseController;
import cn.codeprobe.api.controller.user.PassportControllerApi;
import cn.codeprobe.result.JSONResult;
import cn.codeprobe.utils.IPUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    @Override
    public JSONResult getSMSCode(String mobile, HttpServletRequest request) throws Exception {
        // 获取用户ip，限制用户只允许在60秒内发送一次短信
        String requestIp = IPUtil.getRequestIp(request);
        redisUtil.setnx60s(MOBILE_SMSCODE + ":" + requestIp, requestIp);
        // 生成六位数随机数字验证码，并发送
        String randomCode = RandomUtil.randomNumbers(6);
//        smsUtil.sendSms(mobile, randomCode);
        // 将验证码存入redis，有效期30分钟，方便后续验证
        redisUtil.set(MOBILE_SMSCODE + ":" + mobile, randomCode, MOBILE_SMSCODE_TIMEOUT);
        return JSONResult.ok();
    }


}
