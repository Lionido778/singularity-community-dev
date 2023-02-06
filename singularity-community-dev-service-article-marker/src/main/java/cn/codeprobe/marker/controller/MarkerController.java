package cn.codeprobe.marker.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.marker.MarkerControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.marker.service.MarkerService;
import cn.codeprobe.result.JsonResult;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class MarkerController extends ApiController implements MarkerControllerApi {

    @Resource
    private MarkerService markerService;

    @Override
    public JsonResult publishHtml(String articleId, String mongoId) {
        // 参数校验
        if (CharSequenceUtil.isBlank(articleId) || CharSequenceUtil.isBlank(mongoId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_PUBLISH_FAILED);
        }
        // 调用service
        String result = markerService.publishHtml(articleId, mongoId);
        return JsonResult.ok(result);
    }

    @Override
    public JsonResult deleteHtml(String articleId) {
        // 参数校验
        if (CharSequenceUtil.isBlank(articleId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_DELETE_FAILED);
        }
        // 调用service
        String result = markerService.deleteHtml(articleId);
        return JsonResult.ok(result);
    }
}
