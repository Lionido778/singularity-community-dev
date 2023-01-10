package cn.codeprobe.admin.controller;

import cn.codeprobe.admin.service.AdminPassportService;
import cn.codeprobe.api.controller.admin.PassportControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.AdminLoginBO;
import cn.codeprobe.result.JsonResult;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 管理员 通行证（注册、登录）
 *
 * @author Lionido
 */
@RestController
public class PassportController extends ApiController implements PassportControllerApi {

    @Resource
    private AdminPassportService adminPassportService;

    @Override
    public JsonResult adminLogin(AdminLoginBO adminLoginBO) {
        if (adminLoginBO != null) {
            String username = adminLoginBO.getUsername();
            String password = adminLoginBO.getPassword();
            String img64 = adminLoginBO.getImg64();
            // 校验前端数据不可以为空
            if (CharSequenceUtil.isBlank(username) && CharSequenceUtil.isBlank(password) && CharSequenceUtil.isBlank(img64)) {
                GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_ALL_NULL_ERROR);
            } else {
                if (CharSequenceUtil.isNotBlank(username) && CharSequenceUtil.isNotBlank(password)
                        && CharSequenceUtil.isBlank(img64)) {
                    // 调用service 走用户名、密码登录
                    adminPassportService.loginByUsernameAndPwd(username, password);
                } else if (CharSequenceUtil.isBlank(username) && CharSequenceUtil.isBlank(password)
                        && CharSequenceUtil.isNotBlank(img64)) {
                    // TODO 调用service,走人脸识别登录
                } else {
                    GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
                }
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_ALL_NULL_ERROR);
        }
        return JsonResult.ok();
    }

}
