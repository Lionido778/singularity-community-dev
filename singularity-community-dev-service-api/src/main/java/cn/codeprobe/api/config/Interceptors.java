package cn.codeprobe.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.codeprobe.api.interceptors.AdminTokenInterceptor;
import cn.codeprobe.api.interceptors.PassportInterceptor;
import cn.codeprobe.api.interceptors.UserActivityInterceptor;
import cn.codeprobe.api.interceptors.UserTokenInterceptor;

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
        registry.addInterceptor(passportInterceptor()).addPathPatterns("/passport/querySMSCode");

        // 检查用户登录状态。若未登录，拦截
        registry.addInterceptor(userTokenInterceptor()).addPathPatterns("/writer/user/queryAccountInfo")
            .addPathPatterns("/writer/user/modifyUserInfo").addPathPatterns("/writer/file/uploadFace")
            .addPathPatterns("/writer/file/uploadSerialsFiles").addPathPatterns("/writer/article/addNewArticle")
            .addPathPatterns("/writer/article/withdraw").addPathPatterns("/writer/article/delete")
            .addPathPatterns("/writer/article/queryPageListArticles");

        // 检查管理员登录状态。若未登录，拦截
        registry.addInterceptor(adminTokenInterceptor()).addPathPatterns("/admin/adminMng/queryAdminIsExist")
            .addPathPatterns("/admin/adminMng/addNewAdmin").addPathPatterns("/admin/adminMng/queryListAdmins")
            .addPathPatterns("/admin/file/uploadToGridFS").addPathPatterns("/admin/file/readFromGridFS")
            .addPathPatterns("/admin/friendLinkMng/addOrModifyFriendLink")
            .addPathPatterns("/admin/friendLinkMng/queryListFriendLinks").addPathPatterns("/admin/friendLinkMng/delete")
            .addPathPatterns("/admin/categoryMng/addOrModifyCategory")
            .addPathPatterns("/admin/categoryMng/queryListCategories").addPathPatterns("/admin/categoryMng/delete")
            .addPathPatterns("/admin/userMng/queryPageListUsers").addPathPatterns("/admin/userMng/queryUserInfo")
            .addPathPatterns("/admin/userMng/freezeUserOrNot").addPathPatterns("/admin/articleMng/doReview")
            .addPathPatterns("/admin/articleMng/queryAllPageListArticles");

        // 检查用户的激活状态,若未激活进行拦截
        // registry.addInterceptor(userActivityInterceptor());
        // .addPathPatterns("categoryMng/getCategories");
    }

}
