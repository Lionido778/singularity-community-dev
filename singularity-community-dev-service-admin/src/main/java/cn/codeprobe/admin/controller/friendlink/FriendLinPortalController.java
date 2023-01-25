package cn.codeprobe.admin.controller.friendlink;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.admin.service.friendlink.FriendLinkPortalService;
import cn.codeprobe.api.controller.admin.friendlink.FriendLinkPortalControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.enums.FriendLink;
import cn.codeprobe.pojo.mo.FriendLinkDO;
import cn.codeprobe.result.JsonResult;

/**
 * @author Lionido
 */
@RestController
public class FriendLinPortalController extends ApiController implements FriendLinkPortalControllerApi {

    @Resource
    private FriendLinkPortalService friendLinkPortalService;

    @Override
    public JsonResult queryListFriendLinks() {
        List<FriendLinkDO> list = friendLinkPortalService.listFriendLinks(FriendLink.UN_DELETED.type);
        return JsonResult.ok(list);
    }

}
