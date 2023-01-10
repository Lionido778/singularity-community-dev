package cn.codeprobe.admin.controller;

import cn.codeprobe.admin.service.AdminService;
import cn.codeprobe.api.controller.admin.AdminControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.result.JsonResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lionido
 */
@RestController
public class AdminController extends ApiController implements AdminControllerApi {

    @Resource
    private AdminService adminService;

    @Override
    public JsonResult adminIsExist(String username) {
        Boolean isExist = adminService.checkAdminIsExist(username);
        if (Boolean.TRUE.equals(isExist)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
        return JsonResult.ok();
    }
}
