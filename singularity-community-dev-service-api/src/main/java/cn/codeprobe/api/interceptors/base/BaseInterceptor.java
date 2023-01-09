package cn.codeprobe.api.interceptors.base;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.utils.RedisUtil;
import cn.hutool.core.text.CharSequenceUtil;

import javax.annotation.Resource;

/**
 * 定义拦截器的 属性，常量
 *
 * @author Lionido
 */
public class BaseInterceptor {

    @Resource
    public RedisUtil redisUtil;

    public static final String MOBILE_SMS_CODE = "mobile_sms_code";
    public static final String REDIS_USER_TOKEN = "user_token";
    public static final String REDIS_USER_INFO = "user_info";
    public static final Integer USER_ACTIVE = UserStatus.ACTIVE.type;


    public Boolean verifyIdAndToken(String userId, String userToken) {
        // 检查header中userId或userToken是否为空
        if (CharSequenceUtil.isBlank(userId) || CharSequenceUtil.isBlank(userToken)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 检查redis 缓存中token是否有效
        String redisToken = redisUtil.get(REDIS_USER_TOKEN + ":" + userId);
        if (CharSequenceUtil.isBlank(redisToken)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.TICKET_INVALID);
        }
        // token是否匹配
        if (!redisToken.equalsIgnoreCase(userToken)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.TICKET_INVALID);
        }
        return true;
    }

}
