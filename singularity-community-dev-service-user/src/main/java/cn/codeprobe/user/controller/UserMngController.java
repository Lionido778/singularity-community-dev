package cn.codeprobe.user.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.UserMngControllerApi;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.po.AppUserDO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.user.service.UserMngService;
import cn.codeprobe.user.service.UserWriterService;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * 管理中兴： 用户管理相关接口
 *
 * @author Lionido
 */
@RestController
public class UserMngController extends ApiController implements UserMngControllerApi {

    @Resource
    private UserMngService userMngService;
    @Resource
    private UserWriterService userWriterService;

    @Override
    public JsonResult queryPageListUsers(String nickname, Integer status, Date startDate, Date endDate, Integer page,
        Integer pageSize) {
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        // 调用service 获取用户列表
        PagedGridResult pagedGridResult =
            userMngService.pageListUsers(nickname, status, startDate, endDate, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @Override
    public JsonResult queryUserInfo(String userId) {
        // 校验参数
        if (CharSequenceUtil.isBlank(userId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.USER_QUERY_ERROR);
        }
        // 调用service 获取用户信息
        AppUserDO userInfo = userWriterService.getUserInfo(userId);
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
