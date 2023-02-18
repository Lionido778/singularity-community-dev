package cn.codeprobe.user.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.UserWriterControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
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
        // 调用 service 执行查询操作
        UserBasicInfoVO userBasicInfoVO = userWriterService.getUserBasicInfo(userId);
        return JsonResult.ok(userBasicInfoVO);
    }

    @Override
    public JsonResult queryUserAccountInfo(String userId) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 调用 service 执行查询操作
        UserAccountInfoVO userAccountInfoVO = userWriterService.getUserAccountInfo(userId);

        return JsonResult.ok(userAccountInfoVO);
    }

    @Override
    public JsonResult modifyUserAccountInfo(UpdateUserInfoBO updateUserInfoBO) {
        // 执行更新操作
        userWriterService.updateUserAccountInfo(updateUserInfoBO);
        return JsonResult.ok();
    }
}
