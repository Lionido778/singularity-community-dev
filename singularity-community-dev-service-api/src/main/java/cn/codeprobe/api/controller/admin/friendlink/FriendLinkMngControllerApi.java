package cn.codeprobe.api.controller.admin.friendlink;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.pojo.bo.FriendLinkBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 管理中心: 友情链接管理
 *
 * @author Lionido
 */
@Api(value = "友情链接相关接口", tags = "友情链接相关接口")
@RequestMapping("/admin/friendLinkMng")
public interface FriendLinkMngControllerApi {

    /**
     * 新增或更新友情链接
     *
     * @param friendLinkBO 友情链接表单
     * @param result 表单验证结果
     * @return yes / no
     */
    @PostMapping("/addOrModifyFriendLink")
    @ApiOperation(value = "新增管理员", notes = "新增管理员")
    public JsonResult addOrModifyFriendLink(@RequestBody @Valid FriendLinkBO friendLinkBO, BindingResult result);

    /**
     * 获取所有友情链接列表
     *
     * @return 所有友情链接
     */
    @PostMapping("/queryListFriendLinks")
    @ApiOperation(value = "获取友情链接列表", notes = "获取友情链接列表")
    public JsonResult queryListFriendLinks();

    /**
     * 删除友情链接
     *
     * @param linkId 友情链接ID
     * @return yes / no
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除友情链接", notes = "删除友情链接")
    public JsonResult deleteFriendLink(@RequestParam String linkId);

}
