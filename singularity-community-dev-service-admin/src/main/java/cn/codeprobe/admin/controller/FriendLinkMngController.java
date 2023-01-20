package cn.codeprobe.admin.controller;

import cn.codeprobe.admin.service.FriendLinkService;
import cn.codeprobe.api.controller.admin.FriendLinkMngControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.pojo.bo.FriendLinkBO;
import cn.codeprobe.result.JsonResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Lionido
 */
@RestController
public class FriendLinkMngController extends ApiController implements FriendLinkMngControllerApi {

    @Resource
    private FriendLinkService friendLinkService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult addOrModifyFriendLink(FriendLinkBO friendLinkBO, @NotNull BindingResult result) {
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
    public JsonResult queryListFriendLinks() {
        return JsonResult.ok(friendLinkService.listFriendLinks());
    }

    @Override
    public JsonResult deleteFriendLink(String linkId) {
        friendLinkService.removeFriendLink(linkId);
        return JsonResult.ok();
    }
}
