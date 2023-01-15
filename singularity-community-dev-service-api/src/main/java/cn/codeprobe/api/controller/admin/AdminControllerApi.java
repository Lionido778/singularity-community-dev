package cn.codeprobe.api.controller.admin;


import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @author Lionido
 */
@Api(value = "管理中心相关接口", tags = "管理中心相关接口")
@RequestMapping("/adminMng")
public interface AdminControllerApi {


    @PostMapping("/adminIsExist")
    @ApiOperation(value = "管理员是否存在", notes = "管理员是否存在")
    public JsonResult adminIsExist(@RequestParam String username);


    @PostMapping("/addNewAdmin")
    @ApiOperation(value = "添加管理员", notes = "添加管理员")
    public JsonResult addNewAdmin(@RequestBody @Valid NewAdminBO newAdminBO, BindingResult result);


    @PostMapping("/getAdminList")
    @ApiOperation(value = "获取管理员列表", notes = "获取管理员列表")
    public JsonResult getAdminList(@RequestParam String page, String pageSize);

}



