package cn.codeprobe.api.interceptors;

import cn.codeprobe.api.interceptors.base.BaseInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 会话拦截（判断用户是否是登录状态）
 * 有些接口必须在用户注册登录之后才可以访问
 */
public class UserTokenInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");
        // 校验用户是否是登陆状态
        return verifyIdAndToken(userId, userToken);
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
