package cn.codeprobe.admin.controller;

import cn.codeprobe.admin.service.FriendLinkService;
import cn.codeprobe.api.controller.admin.FriendLinkControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.pojo.bo.FriendLinkBO;
import cn.codeprobe.result.JsonResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Lionido
 */
@RestController
public class FriendLinkController extends ApiController implements FriendLinkControllerApi {

    @Resource
    private FriendLinkService friendLinkService;

    @Override
    public JsonResult saveOrUpdateFriendLink(FriendLinkBO friendLinkBO, BindingResult result) {
        // 校验 BO 数据
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return JsonResult.errorMap(map);
        }
        // 调用 service 保存或更新
        friendLinkService.saveOrUpdateFriendLink(friendLinkBO);
        return JsonResult.ok();
    }

    @Override
    public JsonResult getFriendLinkList() {
        return JsonResult.ok(friendLinkService.getFriendLinks());
    }

    @Override
    public JsonResult deleteFriendLink(String linkId) {
        friendLinkService.delete(linkId);
        return JsonResult.ok();
    }
}
