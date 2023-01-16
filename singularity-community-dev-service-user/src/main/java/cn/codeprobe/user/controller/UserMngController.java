package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.UserMngControllerApi;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.user.service.UserMngService;
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
}
