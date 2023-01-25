package cn.codeprobe.api.controller.admin.category;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 创作中心: 文章分类相关接口
 *
 * @author Lionido
 */
@Api(value = "文章分类相关接口", tags = "文章分类相关接口")
@RequestMapping("/writer/category")
public interface CategoryWriterControllerApi {

    /**
     * 创作中心：获取分类列表
     *
     * @return 所有分类列表
     */
    @GetMapping("/queryCategories")
    @ApiOperation(value = "创作中心获取分类列表", notes = "创作中心获取分类列表", httpMethod = "GET")
    public JsonResult queryCategories();

}
