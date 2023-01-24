package cn.codeprobe.api.interceptors.base;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.utils.IpUtil;
import cn.codeprobe.utils.RedisUtil;
import cn.hutool.core.text.CharSequenceUtil;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公用拦截器
 * 自定义拦截器的属性，常量
 *
 * @author Lionido
 */
public class ApiInterceptor {

    /**
     * sms
     */
    public static final String MOBILE_SMS_CODE = "mobile_sms_code";
    /**
     * token
     */
    public static final String REDIS_USER_TOKEN = "user_token";
    public static final String REDIS_ADMIN_TOKEN = "admin_token";
    /**
     * role
     */
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    /**
     * header
     */
    public static final String HEADER_USER_ID = "headerUserId";
    public static final String HEADER_USER_TOKEN = "headerUserToken";
    public static final String HEADER_ADMIN_ID = "adminUserId";
    public static final String HEADER_ADMIN_TOKEN = "adminUserToken";
    public static final String REDIS_USER_INFO = "user_info";
    public static final Integer USER_ACTIVE = UserStatus.ACTIVE.type;
    /**
     * 登录状态
     */
    public static final Boolean LOGGED = true;
    public static final Boolean UN_LOGGED = false;
    @Resource
    public RedisUtil redisUtil;
    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;

    /**
     * 登录状态检查
     *
     * @param id    用户、管理员id
     * @param token 令牌
     * @param role  用户或管理员
     * @return true 放行请求；false 拦截请求
     */
    public void checkLoginStatus(String id, String token, String role) {
        verifyIdAndToken(id, token, role);
    }

    /**
     * 验证 token
     *
     * @param id    用户、管理员id
     * @param token 令牌
     * @param role  用户或管理员
     */
    public void verifyIdAndToken(String id, String token, String role) {
        // 检查header中 id 或 token 是否为空
        if (CharSequenceUtil.isBlank(id) || CharSequenceUtil.isBlank(token)) {
            // 打印拦截日志
            recordInterceptLog(UN_LOGGED, role, id, token);
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 检查token是否过了有效期（redis缓存中token是否存在）
        String redisToken = "";
        if (role.equalsIgnoreCase(ROLE_ADMIN)) {
            // admin token
            redisToken = redisUtil.get(REDIS_ADMIN_TOKEN + ":" + id);
        } else if (role.equalsIgnoreCase(ROLE_USER)) {
            // user token
            redisToken = redisUtil.get(REDIS_USER_TOKEN + ":" + id);
        }
        if (CharSequenceUtil.isBlank(redisToken)) {
            // 打印拦截日志
            recordInterceptLog(UN_LOGGED, role, id, token);
            GlobalExceptionManage.internal(ResponseStatusEnum.TICKET_INVALID);
        }
        // 检查token是否匹配
        if (!redisToken.equalsIgnoreCase(token)) {
            // 打印拦截日志
            recordInterceptLog(UN_LOGGED, role, id, token);
            GlobalExceptionManage.internal(ResponseStatusEnum.TICKET_INVALID);
        }
    }


    /**
     * 记录打印 拦截放行日记
     *
     * @param isLogged 是否是登录状态
     * @param role     角色（管理员、用户）
     * @param id       主键ID
     * @param token    令牌
     */
    public void recordInterceptLog(boolean isLogged, String role, String id, String token) {
        // 获取请求路径
        String requestUri = request.getRequestURI();
        System.out.println("=====================================================================");
        // 获取请求IP
        String requestIp = IpUtil.getRequestIp(request);
        if (isLogged) {
            System.out.println("IP: " + requestIp);
            System.out.println("ACCESS_URL：" + requestUri + " 请求已被放行！");
        } else {
            System.out.println("IP: " + requestIp);
            System.out.println("ACCESS_URL：" + requestUri + " 请求已被拦截！");
        }
        System.out.println(role + "TokenInterceptor - " + role + "Id = " + id);
        System.out.println(role + "TokenInterceptor - " + role + "Token = " + token);
        System.out.println("=====================================================================");
    }

    /**
     * 从cookie中取值
     *
     * @param request 请求
     * @param key     cookie name
     * @return
     */
    public String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
