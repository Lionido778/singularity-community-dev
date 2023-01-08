package cn.codeprobe.api.controller.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Lionido
 */
@Api(value = "文件项目测试接口", tags = "文件项目测试接口")
@RequestMapping("/test/file")
public interface TestFileControllerApi {

    @ApiOperation(value = "文件项目测试接口", notes = "文件项目测试接口", httpMethod = "GET")
    @GetMapping("/hello")
    Object hello();

}
