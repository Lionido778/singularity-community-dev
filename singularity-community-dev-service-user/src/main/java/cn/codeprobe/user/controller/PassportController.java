package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.PassportControllerApi;
import cn.codeprobe.api.threadlocal.SubjectContext;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.pojo.po.AppUserDO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.user.service.UserPassportService;
import cn.codeprobe.utils.RegexUtil;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Lionido
 */
@RestController
public class PassportController extends ApiController implements PassportControllerApi {

    @Resource
    private UserPassportService userPassportService;

    @Override
    public JsonResult querySmsCode(String mobile) {
        // 校验数据
        if (CharSequenceUtil.isBlank(mobile)) {
            return JsonResult.errorCustom(ResponseStatusEnum.SMS_MOBILE_BLANK);
            // 校验手机号码格式
        } else if (!RegexUtil.isMobile(mobile)) {
            return JsonResult.errorCustom(ResponseStatusEnum.SMS_MOBILE_FORMAT_ERROR);
        }
        // 调用 service 执行获取验证码操作
        userPassportService.getSmsCode(mobile);
        return JsonResult.ok();
    }

    @Override
    public JsonResult registerLogin(RegisterLoginBO registerLoginBO, BindingResult result) {
        // 校验BO数据
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JsonResult.errorMap(errorMap);
        }
        // 调用 service 执行注册登陆
        AppUserDO appUserDO = userPassportService.registerLogin(registerLoginBO);
        // 保存 Subject 到 ThreadLocal 异步线程
        if (!SubjectContext.checkHasUser()) {
            SubjectContext.setUser(appUserDO);
        }
        System.out.println(SubjectContext.getUser());
        return JsonResult.ok(appUserDO.getActiveStatus());
    }

    @Override
    public JsonResult logout(String userId) {
        userPassportService.userLogout(userId);
        // 从 ThreadLocal 异步线程 中移除 Subject
        SubjectContext.removeUser();
        return JsonResult.ok();
    }

}
