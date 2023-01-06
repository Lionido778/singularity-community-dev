package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.BaseController;
import cn.codeprobe.api.controller.user.UserControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalException;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.vo.UserAccountInfoVO;
import cn.codeprobe.result.JSONResult;
import cn.codeprobe.user.service.impl.UserServiceImpl;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController extends BaseController implements UserControllerApi {

    @Resource
    private UserServiceImpl userService;

    @Override
    public JSONResult getUserInfo(String userId) {
        // 校验 userId
        if (StrUtil.isBlank(userId)) {
            GlobalException.Internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 执行查询操作
        AppUser user = userService.queryUserById(userId);
        // pojo -> vo
        UserAccountInfoVO userAccountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, userAccountInfoVO);
        return JSONResult.ok(userAccountInfoVO);
    }
}
