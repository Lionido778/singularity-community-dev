package cn.codeprobe.api.controller.admin;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.pojo.bo.AdminLoginBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 管理中心: 通行证管理
 *
 * @author Lionido
 */
@Api(value = "管理员登录接口", tags = "管理员登录接口")
@RequestMapping("/passport")
public interface AdminPassportControllerApi {

    /**
     * 管理员普通登录（用户名密码）
     *
     * @param adminLoginBO 管理员登陆表单
     * @param result 表单验证结果
     * @return yes ./ no
     */
    @PostMapping("/adminCommonLogin")
    @ApiOperation(value = "管理员用户名密码登录接口", notes = "管理员使用用户名密码登录")
    public JsonResult adminLogin(@RequestBody AdminLoginBO adminLoginBO, BindingResult result);

    /**
     * 管理员人脸识别登录
     *
     * @param adminLoginBO 管理员登陆表单
     * @param result 表单验证结果
     * @return yes ./ no
     */
    @PostMapping("/adminFaceLogin")
    @ApiOperation(value = "管理员人脸识别登录接口", notes = "管理员使用人脸识别登录")
    public JsonResult adminFaceLogin(@RequestBody AdminLoginBO adminLoginBO, BindingResult result);

    /**
     * 管理员退出登录
     *
     * @param adminId 管理员ID
     * @return yes / no
     */
    @PostMapping("/adminLogout")
    @ApiOperation(value = "管理员退出登录接口", notes = "管理员退出登录")
    public JsonResult adminLogout(@RequestParam String adminId);

}
