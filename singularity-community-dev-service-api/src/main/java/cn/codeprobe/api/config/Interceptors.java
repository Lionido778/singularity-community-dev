package cn.codeprobe.api.config;

import cn.codeprobe.api.interceptors.PassportInterceptor;
import cn.codeprobe.api.interceptors.UserActivityInterceptor;
import cn.codeprobe.api.interceptors.UserTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置、注册自定义拦截器
 *
 * @author Lionido
 */
@Configuration
public class Interceptors implements WebMvcConfigurer {

    @Bean
    public PassportInterceptor passportInterceptor() {
        return new PassportInterceptor();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    @Bean
    public UserActivityInterceptor userActivityInterceptor() {
        return new UserActivityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 检查用户发送短信请求是否在限制时间内，若是，拦截请求
        registry.addInterceptor(passportInterceptor())
                .addPathPatterns("/passport/getSMSCode");
        // 检查用户登录状态,若未登录，要进行拦截
        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo")
                .addPathPatterns("/file/uploadFace");
        // 检查用户的激活状态,若未激活进行拦截
        // registry.addInterceptor(userActivityInterceptor())
        //        .addPathPatterns("/user/getAccountInfo")
    }
}
