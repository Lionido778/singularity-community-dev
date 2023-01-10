package cn.codeprobe.api.controller.admin;


import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lionido
 */
@Api(value = "管理中心相关接口", tags = "管理中心相关接口")
@RequestMapping("/adminMng")
public interface AdminControllerApi {


    @PostMapping("/adminIsExist")
    @ApiOperation(value = "管理员是否存在接口", notes = "管理员是否存在接口")
    public JsonResult adminIsExist(@RequestParam String username);

}



