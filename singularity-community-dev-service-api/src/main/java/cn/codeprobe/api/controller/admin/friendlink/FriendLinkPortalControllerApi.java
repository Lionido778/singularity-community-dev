package cn.codeprobe.api.controller.admin.friendlink;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 门户: 友情链接相关接口
 *
 * @author Lionido
 */
@Api(value = "友情链接相关接口", tags = "友情链接相关接口")
@RequestMapping("/portal/friendLink")
public interface FriendLinkPortalControllerApi {

    /**
     * 门户：获取所有友情链接列表
     *
     * @return 所有友情链接
     */
    @GetMapping("/queryListFriendLinks")
    @ApiOperation(value = "获取友情链接列表", notes = "获取友情链接列表", httpMethod = "GET")
    JsonResult queryListFriendLinks();
}
