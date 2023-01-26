package cn.codeprobe.user.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.UserWriterControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.pojo.po.AppUserDO;
import cn.codeprobe.pojo.vo.UserAccountInfoVO;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.user.service.impl.UserWriterServiceImpl;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class UserWriterController extends ApiController implements UserWriterControllerApi {

    @Resource
    private UserWriterServiceImpl userWriterService;

    @Override
    public JsonResult queryUserBasicInfo(String userId) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 执行查询操作
        AppUserDO user = userWriterService.getUserInfo(userId);
        UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
        // pojo -> vo
        BeanUtils.copyProperties(user, userBasicInfoVO);
        return JsonResult.ok(userBasicInfoVO);
    }

    @Override
    public JsonResult queryUserAccountInfo(String userId) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 执行查询操作
        AppUserDO user = userWriterService.getUserInfo(userId);
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
        userWriterService.updateUserAccountInfo(updateUserInfoBO);
        return JsonResult.ok();
    }
}
