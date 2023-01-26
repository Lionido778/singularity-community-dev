package cn.codeprobe.user.controller;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.UserPortalControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.AppUserDO;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.user.service.impl.UserPortalServiceImpl;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class UserPortalController extends ApiController implements UserPortalControllerApi {

    @Resource
    private UserPortalServiceImpl userPortalService;

    @Override
    public JsonResult queryUserBasicInfo(String userId) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 执行查询操作
        AppUserDO user = userPortalService.getUserInfo(userId);
        UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
        // pojo -> vo
        BeanUtils.copyProperties(user, userBasicInfoVO);
        return JsonResult.ok(userBasicInfoVO);
    }

}
