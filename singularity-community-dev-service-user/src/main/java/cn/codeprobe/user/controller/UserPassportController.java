package cn.codeprobe.user.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.UserPassportControllerApi;
import cn.codeprobe.api.threadlocal.SubjectContext;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.pojo.po.User;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.user.service.UserPassportService;
import cn.codeprobe.utils.RegexUtil;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class UserPassportController extends ApiController implements UserPassportControllerApi {

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
    public JsonResult registerLogin(RegisterLoginBO registerLoginBO) {
        // 调用 service 执行注册登陆
        User user = userPassportService.registerLogin(registerLoginBO);
        // 保存 Subject 到 ThreadLocal 异步线程
        if (!SubjectContext.checkHasUser()) {
            SubjectContext.setUser(user);
        }
        System.out.println(SubjectContext.getUser());
        return JsonResult.ok(user.getActiveStatus());
    }

    @Override
    public JsonResult logout(String userId) {
        userPassportService.userLogout(userId);
        // 从 ThreadLocal 异步线程 中移除 Subject
        SubjectContext.removeUser();
        return JsonResult.ok();
    }

}
