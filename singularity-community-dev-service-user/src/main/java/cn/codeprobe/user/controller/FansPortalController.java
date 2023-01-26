package cn.codeprobe.user.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.FansPortalControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.user.service.FansPortalService;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class FansPortalController extends ApiController implements FansPortalControllerApi {

    @Resource
    private FansPortalService fansPortalService;

    @Override
    public JsonResult queryIsFollowed(String writerId, String fanId) {
        // 校验参数
        if (CharSequenceUtil.isBlank(writerId) || CharSequenceUtil.isBlank(fanId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.FANS_FOLLOW_PARAMENT_ERROR);
        }
        // 调用 service 获取是否关注的结果
        Boolean isFollowed = fansPortalService.queryIsFollowed(writerId, fanId);
        return JsonResult.ok(isFollowed);
    }

    @Override
    public JsonResult follow(String writerId, String fanId) {
        // 校验参数
        if (CharSequenceUtil.isBlank(writerId) || CharSequenceUtil.isBlank(fanId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.FANS_FOLLOW_PARAMENT_ERROR);
        }
        // 调用 service
        fansPortalService.followWriter(writerId, fanId);
        return JsonResult.ok();
    }

    @Override
    public JsonResult unFollow(String writerId, String fanId) {
        // 校验参数
        if (CharSequenceUtil.isBlank(writerId) || CharSequenceUtil.isBlank(fanId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.FANS_UN_FOLLOW_PARAMENT_ERROR);
        }
        // 调用 service
        fansPortalService.unFollowWriter(writerId, fanId);
        return JsonResult.ok();
    }

}
