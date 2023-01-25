package cn.codeprobe.admin.controller.friendlink;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.admin.service.friendlink.FriendLinkPortalService;
import cn.codeprobe.api.controller.admin.friendlink.FriendLinkPortalControllerApi;
import cn.codeprobe.api.controller.base.ApiController;

/**
 * @author Lionido
 */
@RestController
public class FriendLinPortalController extends ApiController implements FriendLinkPortalControllerApi {

    @Resource
    private FriendLinkPortalService friendLinkPortalService;

}
