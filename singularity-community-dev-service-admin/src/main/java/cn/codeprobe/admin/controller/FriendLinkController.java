package cn.codeprobe.admin.controller;

import cn.codeprobe.admin.service.FriendLinkService;
import cn.codeprobe.api.controller.admin.FriendLinkControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.pojo.mo.FriendLinkMO;
import cn.codeprobe.result.JsonResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lionido
 */
@RestController
public class FriendLinkController extends ApiController implements FriendLinkControllerApi {

    @Resource
    private FriendLinkService friendLinkService;

    @Override
    public JsonResult saveOrUpdateFriendLink(FriendLinkMO friendLinkMo) {
        friendLinkService.saveOrUpdateFriendLink(friendLinkMo);
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
