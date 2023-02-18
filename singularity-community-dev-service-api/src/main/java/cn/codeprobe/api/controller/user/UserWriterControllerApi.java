package cn.codeprobe.api.controller.user;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import cn.codeprobe.enums.MicroServiceList;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 创作中心：用户相关接口
 *
 * @author Lionido
 */
@Api(value = "创作中心:用户信息相关接口", tags = "用户信息相关接口")
@RequestMapping("/writer/user")
@FeignClient(value = MicroServiceList.SERVICE_USER_WRITER)
public interface UserWriterControllerApi {

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
     * 获取用户账户信息
     *
     * @param userId 用户ID
     * @return yes / no
     */
    @ApiOperation(value = "获取用户账户信息", notes = "用户账户信息", httpMethod = "POST")
    @PostMapping("/queryAccountInfo")
    JsonResult queryUserAccountInfo(@RequestParam String userId);

    /**
     * 更新用户账户信息
     *
     * @param updateUserInfoBO 用户更新表单
     * @return yes / no
     */
    @ApiOperation(value = "更新用户账户信息", notes = "更新用户账户信息", httpMethod = "POST")
    @PostMapping("/modifyUserInfo")
    JsonResult modifyUserAccountInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO);

}
