package cn.codeprobe.api.interceptors.base;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.exception.GlobalException;
import cn.codeprobe.utils.RedisUtil;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;

/**
 * 定义拦截器的 属性，常量
 */
public class BaseInterceptor {

    @Resource
    public RedisUtil redisUtil;

    public final String MOBILE_SMS_CODE = "mobile_sms_code";
    public final String REDIS_USER_TOKEN = "user_token";
    public final String REDIS_USER_INFO = "user_info";
    public final Integer USER_ACTIVE = UserStatus.ACTIVE.type;


    public Boolean verifyIdAndToken(String userId, String userToken) {
        if (StrUtil.isBlank(userId) || StrUtil.isBlank(userToken)) {
            GlobalException.Internal(ResponseStatusEnum.UN_LOGIN);
        }
        String redisToken = redisUtil.get(REDIS_USER_TOKEN + ":" + userId);
        if (StrUtil.isBlank(redisToken)) {
            GlobalException.Internal(ResponseStatusEnum.TICKET_INVALID);
        }
        if (!redisToken.equalsIgnoreCase(userToken)) {
            GlobalException.Internal(ResponseStatusEnum.TICKET_INVALID);
        }
        return true;
    }

}
