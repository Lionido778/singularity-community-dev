package cn.codeprobe.user.service.impl;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.user.service.UserPassportService;
import cn.codeprobe.user.service.UserService;
import cn.codeprobe.user.service.base.UserBaseService;
import cn.codeprobe.utils.IpUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author Lionido
 */
@Service
public class UserPassportServiceImpl extends UserBaseService implements UserPassportService {

    @Resource
    private UserService userService;

    @Override
    public void getSmsCode(String mobile) {
        // 获取用户ip，限制用户只允许在60秒内发送一次短信
        String requestIp = IpUtil.getRequestIp(request);
        redisUtil.setnx60s(MOBILE_SMS_CODE + ":" + requestIp, requestIp);
        // 生成六位数随机数字验证码，并发送
        String randomCode = RandomUtil.randomNumbers(MOBILE_SMS_CODE_DIGITS);
        // smsUtil.sendSms(mobile, randomCode);
        // 将验证码存入redis，有效期30分钟，方便后续验证
        redisUtil.set(MOBILE_SMS_CODE + ":" + mobile, randomCode, MOBILE_SMS_CODE_TIMEOUT);
    }

    @Override
    public AppUser registerLogin(RegisterLoginBO registerLoginBO) {
        // 校验验证码
        String redisSmsCode = redisUtil.get(MOBILE_SMS_CODE + ":" + registerLoginBO.getMobile());
        if (CharSequenceUtil.isBlank(redisSmsCode) || !redisSmsCode.equals(registerLoginBO.getSmsCode())) {
            GlobalExceptionManage.internal(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 通过手机号查询用户是否已经注册
        AppUser appUser = queryAppUserByMobile(registerLoginBO.getMobile());
        // 判断用户是否被冻结
        if (appUser != null && appUser.getActiveStatus().equals(USER_FROZEN)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.USER_FROZEN);
        } else if (appUser == null) {
            // 注册新用户
            appUser = userService.createAppUser(registerLoginBO.getMobile());
        }
        // 登陆成功 配置 token 和 cookie
        userLoginSetting(appUser);
        // 短信验证码有效次数一次，用户成功注册或登陆时，短信验证码作废
        redisUtil.del(MOBILE_SMS_CODE + ":" + registerLoginBO.getMobile());
        return appUser;
    }

    @Override
    public void userLogout(String userId) {
        // 删除 redis 中的token
        redisUtil.del(REDIS_USER_TOKEN + ":" + userId);
        // 删除 cookie
        setCookie(COOKIE_NAME_ID, "", COOKIE_DELETE);
        setCookie(COOKIE_NAME_TOKEN, "", COOKIE_DELETE);
    }

    private void userLoginSetting(AppUser appUser) {
        UUID uToken = UUID.randomUUID();
        String uId = appUser.getId();
        // 保存分布式会话信息Cookie到redis(有效期一个小时)，并响应保存到前端
        redisUtil.set(REDIS_USER_TOKEN + ":" + uId, uToken.toString().trim(), REDIS_USER_TOKEN_TIMEOUT);
        setCookie(COOKIE_NAME_ID, uId, COOKIE_MAX_AGE, domainName);
        setCookie(COOKIE_NAME_TOKEN, uToken.toString(), COOKIE_MAX_AGE, domainName);
    }
}
