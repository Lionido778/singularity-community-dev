package cn.codeprobe.api.interceptors;

import cn.codeprobe.api.interceptors.base.BaseInterceptor;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalException;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.utils.JsonUtils;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户状态拦截器
 * 控制用户在发表文章，评论等操作时必须是激活状态
 * 发生在用户登陆成功后
 */
public class UserActivityInterceptor extends BaseInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = request.getHeader("headerUserId");
        String userJson = redisUtil.get(REDIS_USER_INFO + ":" + userId);
        if (StrUtil.isBlank(userJson)) {
            // 如果redis中没有用户信息缓存，用户未登录，拦截
            GlobalException.Internal(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        if (!JsonUtils.jsonToPojo(userJson, AppUser.class).getActiveStatus().equals(USER_ACTIVE)) {
            // 用户未激活，拦截
            GlobalException.Internal(ResponseStatusEnum.USER_INACTIVE_ERROR);
            return false;
        }
        // 用户是激活状态，放行
        return true;
    }
}
