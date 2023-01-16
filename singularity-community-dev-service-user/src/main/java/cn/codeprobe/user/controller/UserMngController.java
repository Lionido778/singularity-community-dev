package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.UserMngControllerApi;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.user.service.UserMngService;
import cn.codeprobe.user.service.UserService;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * admin 用户管理
 *
 * @author Lionido
 */
@RestController
public class UserMngController extends ApiController implements UserMngControllerApi {

    @Resource
    private UserMngService userMngService;
    @Resource
    private UserService userService;

    @Override
    public JsonResult getUserList(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        // 调用service 获取用户列表
        PagedGridResult pagedGridResult = userMngService.getUserList(
                nickname, status, startDate, endDate, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @Override
    public JsonResult getUserInfo(String userId) {
        // 校验参数
        if (CharSequenceUtil.isBlank(userId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.USER_QUERY_ERROR);
        }
        // 调用service 获取用户信息
        AppUser userInfo = userService.getUserInfo(userId);
        return JsonResult.ok(userInfo);
    }

    @Override
    public JsonResult freezeUserOrNot(String userId, Integer doStatus) {
        // 校验参数
        if (CharSequenceUtil.isBlank(userId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.USER_OPERATION_ERROR);
        }
        if (doStatus == null) {
            return JsonResult.errorCustom(ResponseStatusEnum.USER_OPERATION_ERROR);
        }
        // 调用 service 进行解冻或者冻结操作
        userMngService.freezeUserOrNot(userId, doStatus);

        return JsonResult.ok();
    }
}
