package cn.codeprobe.api.config;


import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Lionido
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    //    http://localhost:8080/swagger-ui.html     原路径
    //    http://localhost:8080/doc.html            新路径

    /**
     * swagger2核心配置 docket
     */
    @Bean
    public Docket createRestApi() {

        Predicate<RequestHandler> testPredicate = RequestHandlerSelectors.basePackage("cn.codeprobe.test.controller");
        Predicate<RequestHandler> userPredicate = RequestHandlerSelectors.basePackage("cn.codeprobe.user.controller");
        Predicate<RequestHandler> filePredicate = RequestHandlerSelectors.basePackage("cn.codeprobe.file.controller");
        Predicate<RequestHandler> adminPredicate = RequestHandlerSelectors.basePackage("cn.codeprobe.admin.controller");

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(Predicates.or(testPredicate, userPredicate, filePredicate, adminPredicate))
//                .apis(Predicates.or(adminPredicate, articlePredicate, userPredicate, filesPredicate))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("奇点社区·接口api")
                .contact(new Contact("奇点社区",
                        "https://www.codeprobe.cn",
                        "aingularity@codeprobe.cn"))
                .description("专为奇点社区·平台提供的api文档")
                .version("1.0.1")
                .termsOfServiceUrl("https://www.codeprobe.cn")
                .build();
    }
}