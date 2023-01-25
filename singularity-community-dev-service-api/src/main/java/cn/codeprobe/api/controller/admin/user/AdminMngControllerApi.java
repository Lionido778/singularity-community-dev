package cn.codeprobe.api.controller.admin.user;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 管理中心: 管理员管理相关接口
 *
 * @author Lionido
 */
@Api(value = "管理中心管理员管理相关接口", tags = "管理中心相关接口")
@RequestMapping("/admin/adminMng")
public interface AdminMngControllerApi {

    /**
     * 查询管理员是否存在
     *
     * @param username 管理员登录名
     * @return 不存在 ok/ 已存在 抛异常
     */
    @PostMapping("/queryAdminIsExist")
    @ApiOperation(value = "管理员是否存在", notes = "管理员是否存在")
    public JsonResult queryAdminIsExist(@RequestParam String username);

    /**
     * 新增管理员用户
     *
     * @param newAdminBO 表单数据
     * @param result 表单验证结果
     * @return ok/no
     */
    @PostMapping("/addNewAdmin")
    @ApiOperation(value = "添加管理员", notes = "添加管理员")
    public JsonResult addNewAdmin(@RequestBody @Valid NewAdminBO newAdminBO, BindingResult result);

    /**
     * 获取管理员分页列表
     *
     * @param page 当前页
     * @param pageSize 当前页查询数量
     * @return 管理员分页列表
     */
    @PostMapping("/queryListAdmins")
    @ApiOperation(value = "获取管理员列表", notes = "获取管理员列表")
    public JsonResult queryListAdmins(@RequestParam String page, String pageSize);

}
