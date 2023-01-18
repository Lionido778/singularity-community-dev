package cn.codeprobe.api.controller.admin;


import cn.codeprobe.pojo.bo.CategoryBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Lionido
 */
@Api(value = "文章分类相关接口", tags = "文章分类相关接口")
@RequestMapping("/categoryMng")
public interface CategoryMngControllerApi {

    /**
     * 新增/更新分类
     *
     * @param categoryBO 分类表单
     * @param result     表单验证结果
     * @return yes / no
     */
    @PostMapping("/addOrModifyCategory")
    @ApiOperation(value = "新增或更新分类", notes = "新增或更新分类")
    public JsonResult addOrModifyCategory(@RequestBody @Valid CategoryBO categoryBO, BindingResult result);

    /**
     * 管理中心获取所有分类列表
     *
     * @return 所有分类列表
     */
    @PostMapping("/queryListCategories")
    @ApiOperation(value = "管理中心获取分类列表", notes = "管理中心获取分类列表", httpMethod = "POST")
    public JsonResult queryListCategories();

    /**
     * 创作中心获取分类列表
     *
     * @return 所有分类列表
     */
    @GetMapping("/queryCategories")
    @ApiOperation(value = "创作中心获取分类列表", notes = "创作中心获取分类列表", httpMethod = "GET")
    public JsonResult queryCategories();

    /**
     * 删除分类
     *
     * @param categoryId 分类Id
     * @return yes / no
     */
    @PostMapping("/deleteCategory")
    @ApiOperation(value = "删除分类", notes = "删除分类")
    public JsonResult deleteCategory(@RequestParam Integer categoryId);

}



