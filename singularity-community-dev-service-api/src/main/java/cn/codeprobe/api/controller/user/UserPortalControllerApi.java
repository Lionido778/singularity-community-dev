package cn.codeprobe.api.controller.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.enums.MicroServiceList;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 门户：用户相关接口
 *
 * @author Lionido
 */
@Api(value = "用户信息相关接口", tags = "用户信息相关接口")
@RequestMapping("/portal/user")
@FeignClient(value = MicroServiceList.SERVICE_USER)
public interface UserPortalControllerApi {

    /**
     * 获取用户基本信息
     *
     * @param userId 用户ID
     * @return yes / no
     */
    @ApiOperation(value = "获取用户基本信息", notes = "用户基本信息", httpMethod = "GET")
    @GetMapping("/queryUserBasicInfo")
    JsonResult queryUserBasicInfo(@RequestParam String userId);

    /**
     * 获取用户信息 map
     *
     * @param userIds 用户 idSetStr
     * @return JsonResult
     */
    @ApiOperation(value = "获取用户基本信息", notes = "用户基本信息", httpMethod = "GET")
    @GetMapping("/queryUserBasicInfoBySet")
    JsonResult queryUserBasicInfoBySet(@RequestParam String userIds);

}
