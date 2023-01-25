package cn.codeprobe.api.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.codeprobe.api.interceptors.base.ApiInterceptor;

/**
 * 会话拦截（判断用户是否是登录状态） 有些接口必须在用户在登陆状态下才可以访问
 *
 * @author Lionido
 */
public class UserTokenInterceptor extends ApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull Object handler) {
        String userId = request.getHeader(HEADER_USER_ID);
        String userToken = request.getHeader(HEADER_USER_TOKEN);
        // 校验用户的登陆状态
        checkLoginStatus(userId, userToken, ROLE_USER);
        // 打印放行日志
        recordInterceptLog(LOGGED, ROLE_USER, userId, userToken);
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
