package cn.codeprobe.api.interceptors;

import cn.codeprobe.api.interceptors.base.BaseInterceptor;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalException;
import cn.codeprobe.utils.IPUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过请求IP拦截用户发送的短信次数，每六十秒发送一次
 */

public class PassportInterceptor extends BaseInterceptor implements HandlerInterceptor {

    /**
     * 请求进入controller 方法之前
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestIp = IPUtil.getRequestIp(request);
        boolean keyIsExist = redisUtil.keyIsExist(MOBILE_SMS_CODE + ":" + requestIp);
        if (keyIsExist) {
            GlobalException.Internal(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            // 拦截
            return false;
        }
        // 放行
        return true;
    }

    /**
     * 进入controller之后，渲染视图前
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 进入controller之后，渲染视图之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
