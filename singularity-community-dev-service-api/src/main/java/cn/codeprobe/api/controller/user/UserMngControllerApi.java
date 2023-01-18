package cn.codeprobe.api.controller.user;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author Lionido
 */
@RequestMapping("/userMng")
@Api(value = "管理中心用户接口")
public interface UserMngControllerApi {

    /**
     * 获取用户分页列表
     *
     * @param nickname     用户昵称
     * @param activeStatus 用户激活状态
     * @param startTime    起始时间
     * @param endTime      截止时间
     * @param page         用户当前页
     * @param pageSize     每页查询数
     * @return 用户列表
     */
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表", httpMethod = "POST")
    @PostMapping("/queryPageListUsers")
    JsonResult queryPageListUsers(@RequestParam String nickname, Integer activeStatus, Date startTime, Date endTime, Integer page, Integer pageSize);

    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return User
     */
    @ApiOperation(value = "查看用户", notes = "查看用户", httpMethod = "POST")
    @PostMapping("/queryUserInfo")
    JsonResult queryUserInfo(@RequestParam String userId);

    /**
     * 冻结或者解冻用户
     *
     * @param userId   用户ID
     * @param doStatus 执行操作状态
     * @return yes / No
     */
    @ApiOperation(value = "冻结或者解冻用户", notes = "冻结或者解冻用户", httpMethod = "POST")
    @PostMapping("/freezeUserOrNot")
    JsonResult freezeUserOrNot(@RequestParam String userId, Integer doStatus);
}
