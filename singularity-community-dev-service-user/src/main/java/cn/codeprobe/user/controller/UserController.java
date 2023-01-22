package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.UserControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.pojo.po.AppUserDO;
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

/**
 * @author Lionido
 */
@RestController
public class UserController extends ApiController implements UserControllerApi {

    @Resource
    private UserServiceImpl userService;

    @Override
    public JsonResult queryUserBasicInfo(String userId) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 执行查询操作
        AppUserDO user = userService.getUserInfo(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        // pojo -> vo
        BeanUtils.copyProperties(user, userInfoVO);
        return JsonResult.ok(userInfoVO);
    }

    @Override
    public JsonResult queryUserAccountInfo(String userId) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 执行查询操作
        AppUserDO user = userService.getUserInfo(userId);
        // pojo -> vo
        UserAccountInfoVO userAccountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, userAccountInfoVO);
        return JsonResult.ok(userAccountInfoVO);
    }

    @Override
    public JsonResult modifyUserAccountInfo(UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
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
