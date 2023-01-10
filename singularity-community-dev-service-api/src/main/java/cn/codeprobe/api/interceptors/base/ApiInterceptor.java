package cn.codeprobe.api.interceptors.base;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.utils.RedisUtil;
import cn.hutool.core.text.CharSequenceUtil;

import javax.annotation.Resource;

/**
 * 公用拦截器
 * 自定义拦截器的属性，常量
 *
 * @author Lionido
 */
public class ApiInterceptor {

    @Resource
    public RedisUtil redisUtil;

    public static final String MOBILE_SMS_CODE = "mobile_sms_code";
    public static final String REDIS_USER_TOKEN = "user_token";
    public static final String REDIS_ADMIN_TOKEN = "admin_token";
    public static final String REDIS_USER_INFO = "user_info";
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    public static final Integer USER_ACTIVE = UserStatus.ACTIVE.type;

    /**
     * 登录状态检查
     *
     * @param id    用户、管理员id
     * @param token 令牌
     * @param role  用户或管理员
     * @return true 放行请求；false 拦截请求
     */
    public Boolean checkLoginStatus(String id, String token, String role) {
        return verifyIdAndToken(id, token, role);
    }

    /**
     * 验证 token
     *
     * @param id    用户、管理员id
     * @param token 令牌
     * @param role  用户或管理员
     * @return true 放行请求；false 拦截请求
     */
    public Boolean verifyIdAndToken(String id, String token, String role) {
        // 检查header中userId或userToken是否为空
        if (CharSequenceUtil.isBlank(id) || CharSequenceUtil.isBlank(token)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 检查redis 缓存中token是否有效
        String redisToken = "";
        if (role.equalsIgnoreCase(ROLE_ADMIN)) {
            // admin token
            redisToken = redisUtil.get(REDIS_ADMIN_TOKEN + ":" + id);
        } else if (role.equalsIgnoreCase(ROLE_USER)) {
            // user token
            redisToken = redisUtil.get(REDIS_USER_TOKEN + ":" + id);
        }
        if (CharSequenceUtil.isBlank(redisToken)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.TICKET_INVALID);
        }
        // token是否匹配
        if (!redisToken.equalsIgnoreCase(token)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.TICKET_INVALID);
        }
        return true;
    }


}
