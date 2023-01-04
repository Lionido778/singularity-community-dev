package cn.codeprobe.api.controller.test;

import cn.codeprobe.result.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "Redis测试接口",tags = "测试Redis是否整合成功")
public interface RedisControllerApi {

    @ApiOperation(value = "Redis测试接口",notes = "Redis测试接口",httpMethod = "GET")
    @GetMapping("/redis")
    JSONResult redis();
}
