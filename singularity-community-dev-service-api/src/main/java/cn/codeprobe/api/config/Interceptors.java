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
        // 拦截用户发送短信请求，限制用户发送短信次数
        registry.addInterceptor(passportInterceptor())
                .addPathPatterns("/passport/getSMSCode");
        // 判断用户是否成功登录,若未登录，进行拦截
        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo");
        // 判断用户是否为激活状态,若未激活进行拦截
//        registry.addInterceptor(userActivityInterceptor())
//                .addPathPatterns("/user/getAccountInfo")
    }
}
