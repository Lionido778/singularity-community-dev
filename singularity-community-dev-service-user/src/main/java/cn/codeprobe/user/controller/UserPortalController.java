package cn.codeprobe.user.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.UserPortalControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.user.service.UserPortalService;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author Lionido
 */
@RestController
public class UserPortalController extends ApiController implements UserPortalControllerApi {

    @Resource
    private UserPortalService userPortalService;

    @Override
    public JsonResult queryUserBasicInfo(String userId) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 调用service
        UserBasicInfoVO userBasicInfoVO = userPortalService.getBasicUserInfo(userId);
        return JsonResult.ok(userBasicInfoVO);
    }

    @Override
    public JsonResult queryUserBasicInfoBySet(String userIds) {
        List<String> idList = JSONUtil.toList(userIds, String.class);
        // 校验 idList
        Map map = null;
        if (!idList.isEmpty()) {
            // 调用service
            map = userPortalService.getUserInfoMapByIdSet(idList);
        }
        String jsonString = JSON.toJSONString(map);
        System.out.println(jsonString);
        return JsonResult.ok(jsonString);
    }
}
