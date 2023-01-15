package cn.codeprobe.api.controller.admin;


import cn.codeprobe.pojo.bo.FriendLinkBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @author Lionido
 */
@Api(value = "友情链接相关接口", tags = "友情链接相关接口")
@RequestMapping("/adminMng/friendLinkMng")
public interface FriendLinkControllerApi {

    @PostMapping("/saveOrUpdateFriendLink")
    @ApiOperation(value = "管理员是否存在", notes = "管理员是否存在")
    public JsonResult saveOrUpdateFriendLink (@RequestBody @Valid FriendLinkBO friendLinkBO, BindingResult result);

    @PostMapping("/getFriendLinkList")
    @ApiOperation(value = "获取友情链接列表", notes = "获取友情链接列表")
    public JsonResult getFriendLinkList();

    @PostMapping("/delete")
    @ApiOperation(value = "删除友情链接", notes = "删除友情链接")
    public JsonResult deleteFriendLink(@RequestParam String linkId);

}



