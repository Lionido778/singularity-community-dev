package cn.codeprobe.api.controller.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 门户：粉丝
 *
 * @author Lionido
 */
@Api(value = "门户:粉丝关注信息相关接口", tags = "粉丝关注信息相关接口")
@RequestMapping("/portal/fans/")
public interface FansPortalControllerApi {

    /**
     * 查询用户是否关注作者
     *
     * @param writerId 作者ID
     * @param fanId 用户ID
     * @return yes / no
     */
    @ApiOperation(value = "查询用户是否关注作者", notes = "查询用户是否关注作者", httpMethod = "POST")
    @PostMapping("/queryIsFollowed")
    JsonResult queryIsFollowed(@RequestParam String writerId, String fanId);

    /**
     * 关注作者
     *
     * @param writerId 作者ID
     * @param fanId 用户ID
     * @return yes / no
     */
    @ApiOperation(value = "关注作者", notes = "关注作者", httpMethod = "POST")
    @PostMapping("/follow")
    JsonResult follow(@RequestParam String writerId, String fanId);

    /**
     * 取关作者
     *
     * @param writerId 作者ID
     * @param fanId 用户ID
     * @return yes / no
     */
    @ApiOperation(value = "取关作者", notes = "取关作者", httpMethod = "POST")
    @PostMapping("/unfollow")
    JsonResult unFollow(@RequestParam String writerId, String fanId);

}
