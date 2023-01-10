package cn.codeprobe.api.interceptors;

import cn.codeprobe.api.interceptors.base.ApiInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 会话拦截（判断管理员的登录状态，如果未登录需要拦截并取消请求）
 * 有些接口必须确保管理员是在登录状态下才可以访问
 *
 * @author Lionido
 */
public class AdminTokenInterceptor extends ApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String adminId = request.getHeader("adminUserId");
        String adminToken = request.getHeader("adminUserToken");
        // 校验用户的登陆状态
        return verifyIdAndToken(adminId, adminToken, ROLE_ADMIN);
    }


    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
