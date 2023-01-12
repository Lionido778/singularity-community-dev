package cn.codeprobe.api.interceptors;

import cn.codeprobe.api.interceptors.base.ApiInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Header中获取 id、token 失效时，使用 cookie
 *
 * @author Lionido
 */
public class AdminCookieInterceptor extends ApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String adminId = getCookie(request, HEADER_ADMIN_ID);
        String adminToken = request.getHeader(HEADER_ADMIN_TOKEN);
        // 校验管理员的登陆状态
        Boolean isLogged = checkLoginStatus(adminId, adminToken, ROLE_ADMIN);
        // 打印拦截日志
        recordInterceptLog(isLogged, ROLE_ADMIN, adminId, adminToken);
        return isLogged;
    }
}
