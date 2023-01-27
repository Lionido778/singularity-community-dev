package cn.codeprobe.user.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.user.FansWriterControllerApi;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.enums.UserSex;
import cn.codeprobe.pojo.vo.FansCountChartVO;
import cn.codeprobe.pojo.vo.FansRegionRatioVO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.user.service.FansWriterService;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class FansWriterController extends ApiController implements FansWriterControllerApi {

    @Resource
    private FansWriterService fansWriterService;

    @Override
    public JsonResult queryPageListFans(String writerId, Integer page, Integer pageSize) {
        // 校验数据并初始化
        if (CharSequenceUtil.isBlank(writerId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.FANS_PARAMENT_ERROR);
        }
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        // 调用 service 查询用户粉丝列表
        PagedGridResult pagedGridResult = fansWriterService.pageListFans(writerId, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @Override
    public JsonResult queryRatioBySex(String writerId) {
        // 校验数据并初始化
        if (CharSequenceUtil.isBlank(writerId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.FANS_PARAMENT_ERROR);
        }
        // 调用 service 根据性别查询用户粉丝列表
        Long manCounts = fansWriterService.countFansBySex(writerId, UserSex.MALE.type);
        Long womanCounts = fansWriterService.countFansBySex(writerId, UserSex.FEMALE.type);
        FansCountChartVO fansCountChartVO = new FansCountChartVO();
        fansCountChartVO.setManCounts(manCounts);
        fansCountChartVO.setWomanCounts(womanCounts);
        return JsonResult.ok(fansCountChartVO);
    }

    @Override
    public JsonResult queryRatioByRegion(String writerId) {
        // 校验数据并初始化
        if (CharSequenceUtil.isBlank(writerId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.FANS_PARAMENT_ERROR);
        }
        // 调用 service 根据性别查询用户粉丝列表
        List<FansRegionRatioVO> list = fansWriterService.countFansByRegion(writerId);
        return JsonResult.ok(list);
    }
}
