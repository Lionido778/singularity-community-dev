package cn.codeprobe.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "测试接口",tags = "项目测试接口")
public interface HelloControllerApi {

    @ApiOperation(value = "项目测试Hello接口",notes = "项目测试Hello接口",httpMethod = "GET")
    @GetMapping("/helloapi")
    Object Hello();
}
