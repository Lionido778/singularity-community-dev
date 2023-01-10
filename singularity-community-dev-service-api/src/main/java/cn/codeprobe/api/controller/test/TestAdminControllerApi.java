package cn.codeprobe.api.controller.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Lionido
 */
@Api(value = "管理中心项目测试接口", tags = "管理中心项目测试接口")
@RequestMapping("/admin/test")
public interface TestAdminControllerApi {

    @ApiOperation(value = "管理中心项目测试接口", notes = "管理中心项目测试接口", httpMethod = "GET")
    @GetMapping("/hello")
    Object hello();

}
