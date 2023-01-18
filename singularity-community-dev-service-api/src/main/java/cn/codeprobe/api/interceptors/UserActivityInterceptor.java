package cn.codeprobe.api.interceptors;

import cn.codeprobe.api.interceptors.base.ApiInterceptor;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.AppUserDO;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户状态拦截器
 * 控制用户在发表文章，评论等操作时必须是激活状态
 * 发生在用户登陆成功后
 *
 * @author Lionido
 */
public class UserActivityInterceptor extends ApiInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {

        String userId = request.getHeader("headerUserId");
        String userJson = redisUtil.get(REDIS_USER_INFO + ":" + userId);
        if (CharSequenceUtil.isBlank(userJson)) {
            // 如果redis中没有用户信息缓存，用户未登录，拦截
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        AppUserDO appUserDO = JSONUtil.toBean(userJson, AppUserDO.class);
        if (appUserDO != null) {
            if (!appUserDO.getActiveStatus().equals(USER_ACTIVE)) {
                // 用户未激活，拦截
                GlobalExceptionManage.internal(ResponseStatusEnum.USER_INACTIVE_ERROR);
                return false;
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 用户是激活状态，放行
        return true;
    }
}
