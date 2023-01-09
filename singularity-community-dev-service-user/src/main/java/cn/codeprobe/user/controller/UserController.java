package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.BaseController;
import cn.codeprobe.api.controller.user.UserControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.pojo.vo.UserAccountInfoVO;
import cn.codeprobe.pojo.vo.UserInfoVO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.user.service.impl.UserServiceImpl;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class UserController extends BaseController implements UserControllerApi {

    @Resource
    private UserServiceImpl userService;

    @Override
    public JsonResult getUserBasicInfo(String userId) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 执行查询操作
        AppUser user = userService.getUserInfo(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        // pojo -> vo
        BeanUtils.copyProperties(user, userInfoVO);
        return JsonResult.ok(userInfoVO);
    }

    @Override
    public JsonResult getUserAccountInfo(String userId) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 执行查询操作
        AppUser user = userService.getUserInfo(userId);
        // pojo -> vo
        UserAccountInfoVO userAccountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, userAccountInfoVO);
        return JsonResult.ok(userAccountInfoVO);
    }

    @Override
    public JsonResult updateUserAccountInfo(UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
        // 校验BO数据
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JsonResult.errorMap(errorMap);
        }
        // 执行更新操作
        userService.updateUserAccountInfo(updateUserInfoBO);
        return JsonResult.ok();
    }
}
