package cn.codeprobe.admin.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.admin.service.AdminPassportService;
import cn.codeprobe.api.controller.admin.AdminPassportControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.bo.AdminLoginBO;
import cn.codeprobe.result.JsonResult;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * 管理员 通行证（注册、登录）
 *
 * @author Lionido
 */
@RestController
public class AdminPassportController extends ApiController implements AdminPassportControllerApi {

    @Resource
    private AdminPassportService adminPassportService;

    @Override
    public JsonResult adminLogin(AdminLoginBO adminLoginBO) {
        // 校验登陆参数用户名密码 不可以为空
        String username = adminLoginBO.getUsername();
        String password = adminLoginBO.getPassword();
        if (CharSequenceUtil.isBlank(username) && CharSequenceUtil.isBlank(password)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ADMIN_ALL_NULL_ERROR);
        }
        // 调用 service 进行用户名、密码登录
        adminPassportService.loginByUsernameAndPwd(username, password);
        return JsonResult.ok();
    }

    @Override
    public JsonResult adminFaceLogin(AdminLoginBO adminLoginBO) {
        // 校验登陆参数,人脸图像(img64) 不可以为空
        String img64Face = adminLoginBO.getImg64();
        if (CharSequenceUtil.isBlank(img64Face)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }
        // 调用 service 进行人脸识别登录
        adminPassportService.loginByFace(adminLoginBO.getUsername(), img64Face);
        return JsonResult.ok();
    }

    @Override
    public JsonResult adminLogout(String adminId) {
        adminPassportService.adminLogout(adminId);
        return JsonResult.ok();
    }

}
