package cn.codeprobe.admin.controller.friendlink;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.admin.service.friendlink.FriendLinkMngService;
import cn.codeprobe.api.controller.admin.friendlink.FriendLinkMngControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.pojo.bo.NewFriendLinkBO;
import cn.codeprobe.result.JsonResult;

/**
 * @author Lionido
 */
@RestController
public class FriendLinkMngController extends ApiController implements FriendLinkMngControllerApi {

    @Resource
    private FriendLinkMngService friendLinkMngService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult addOrModifyFriendLink(NewFriendLinkBO newFriendLinkBO) {
        // 调用 service 保存或更新
        friendLinkMngService.saveOrUpdateFriendLink(newFriendLinkBO);
        return JsonResult.ok();
    }

    @Override
    public JsonResult queryListFriendLinks() {
        return JsonResult.ok(friendLinkMngService.listFriendLinks());
    }

    @Override
    public JsonResult deleteFriendLink(String linkId) {
        friendLinkMngService.removeFriendLink(linkId);
        return JsonResult.ok();
    }
}
