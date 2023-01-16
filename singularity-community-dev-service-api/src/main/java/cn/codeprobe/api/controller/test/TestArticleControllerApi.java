package cn.codeprobe.api.controller.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Lionido
 */
@Api(value = "文章测试接口", tags = "文章测试接口")
@RequestMapping("/article/test")
public interface TestArticleControllerApi {

    @ApiOperation(value = "文章测试接口", notes = "文章测试接口", httpMethod = "GET")
    @GetMapping("/hello")
    Object hello();

}
