package cn.codeprobe.api.controller.marker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Lionido
 */
@Api(value = "静态文件服务相关接口", tags = "静态文件服务相关接口")
@RequestMapping("/marker/file")
public interface MarkerControllerApi {

    /**
     * 将静态文章写入到指定位置（发布静态文章)
     *
     * @param articleId 文章ID
     * @param mongoId mongoId
     * @return ok
     */
    @ApiOperation(value = "静态文章生成", notes = "静态文章生成", httpMethod = "GET")
    @GetMapping("/publishHtml")
    JsonResult publishHtml(@RequestParam String articleId, String mongoId);

    /**
     * 删除发布静态文章
     *
     * @param articleId 文章ID
     * @return ok
     */
    @ApiOperation(value = "删除发布静态文章", notes = "删除发布静态文章", httpMethod = "GET")
    @GetMapping("/deleteHtml")
    JsonResult deleteHtml(@RequestParam String articleId);

}
