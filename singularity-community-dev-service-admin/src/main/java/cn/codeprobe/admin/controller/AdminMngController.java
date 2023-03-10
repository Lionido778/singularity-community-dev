package cn.codeprobe.admin.controller;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.admin.service.AdminService;
import cn.codeprobe.api.controller.admin.user.AdminMngControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class AdminMngController extends ApiController implements AdminMngControllerApi {

    @Resource
    private AdminService adminService;

    @Override
    public JsonResult queryAdminIsExist(String username) {
        Boolean isExist = adminService.checkAdminIsExist(username);
        if (Boolean.TRUE.equals(isExist)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
        return JsonResult.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult addNewAdmin(NewAdminBO newAdminBO) {
        // 校验两次密码是否一致
        if (!newAdminBO.getPassword().equalsIgnoreCase(newAdminBO.getConfirmPassword())) {
            JsonResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
        }
        // 校验管理员用户唯一性
        Boolean isExist = adminService.checkAdminIsExist(newAdminBO.getUsername());
        if (Boolean.TRUE.equals(isExist)) {
            JsonResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
        // 添加管理员用户
        adminService.saveAdminUser(newAdminBO);
        return JsonResult.ok();
    }

    @Override
    public JsonResult queryListAdmins(String page, String pageSize) {
        // 参数校验
        if (CharSequenceUtil.isBlank(page) || CharSequenceUtil.isBlank(pageSize)) {
            JsonResult.errorCustom(ResponseStatusEnum.ADMIN_PAGE_NULL_ERROR);
        }
        // 封装 分页数据
        PagedGridResult pagedGridResult =
            adminService.pageListAdminUsers(Integer.parseInt(page), Integer.parseInt(pageSize));
        return JsonResult.ok(pagedGridResult);
    }
}
