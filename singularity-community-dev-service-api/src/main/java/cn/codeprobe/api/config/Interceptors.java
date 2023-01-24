package cn.codeprobe.api.config;

import cn.codeprobe.api.interceptors.AdminTokenInterceptor;
import cn.codeprobe.api.interceptors.PassportInterceptor;
import cn.codeprobe.api.interceptors.UserActivityInterceptor;
import cn.codeprobe.api.interceptors.UserTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册、配置自定义拦截器
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
    public AdminTokenInterceptor adminTokenInterceptor() {
        return new AdminTokenInterceptor();
    }

    @Bean
    public UserActivityInterceptor userActivityInterceptor() {
        return new UserActivityInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 检查用户发送短信请求是否在限制时间内。若是，拦截请求
        registry.addInterceptor(passportInterceptor())
                .addPathPatterns("/passport/querySMSCode");
        // 检查用户登录状态。若未登录，拦截
        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/user/queryAccountInfo")
                .addPathPatterns("/user/modifyUserInfo")
                .addPathPatterns("/file/uploadFace")
                .addPathPatterns("/file/uploadSerialsFiles")
                .addPathPatterns("/article/addNewArticle")
                .addPathPatterns("/article/withdraw")
                .addPathPatterns("/article/queryPageListArticles");

        // 检查管理员登录状态。若未登录，拦截
        registry.addInterceptor(adminTokenInterceptor())
                .addPathPatterns("/adminMng/queryAdminIsExist")
                .addPathPatterns("/adminMng/addNewAdmin")
                .addPathPatterns("/adminMng/queryListAdmins")
                .addPathPatterns("/file/uploadToGridFS")
                .addPathPatterns("/file/readFromGridFS")
                .addPathPatterns("/friendLinkMng/addOrModifyFriendLink")
                .addPathPatterns("/friendLinkMng/queryListFriendLinks")
                .addPathPatterns("/friendLinkMng/deleteFriendLink")
                .addPathPatterns("/categoryMng/addOrModifyCategory")
                .addPathPatterns("/categoryMng/queryListCategories")
                .addPathPatterns("/categoryMng/deleteCategory")
                .addPathPatterns("/userMng/queryPageListUsers")
                .addPathPatterns("/userMng/queryUserInfo")
                .addPathPatterns("/userMng/freezeUserOrNot")
                .addPathPatterns("/article/doReview")
                .addPathPatterns("/article/queryAllPageListArticles");

        // 检查用户的激活状态,若未激活进行拦截
        //registry.addInterceptor(userActivityInterceptor());
        //.addPathPatterns("categoryMng/getCategories");
    }

}
