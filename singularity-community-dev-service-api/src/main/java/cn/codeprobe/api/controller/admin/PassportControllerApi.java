package cn.codeprobe.api.controller.admin;

import cn.codeprobe.pojo.bo.AdminLoginBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Lionido
 */

@Api(value = "管理员登录接口", tags = "管理员登录接口")
@RequestMapping("/passport")
public interface PassportControllerApi {

    @PostMapping("/adminLogin")
    @ApiOperation(value = "登录接口", notes = "登录接口")
    public JsonResult adminLogin(@RequestBody AdminLoginBO adminLoginBO);

}
