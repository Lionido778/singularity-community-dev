package cn.codeprobe.api.controller.admin.category;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.pojo.bo.NewCategoryBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 管理中心: 文章分类管理
 *
 * @author Lionido
 */
@Api(value = "文章分类相关接口", tags = "文章分类相关接口")
@RequestMapping("/admin/categoryMng")
public interface CategoryMngControllerApi {

    /**
     * 新增/更新分类
     *
     * @param newCategoryBO 分类表单
     * @param result 表单验证结果
     * @return yes / no
     */
    @PostMapping("/addOrModifyCategory")
    @ApiOperation(value = "新增或更新分类", notes = "新增或更新分类")
    public JsonResult addOrModifyCategory(@RequestBody @Valid NewCategoryBO newCategoryBO);

    /**
     * 管理中心获取所有分类列表
     *
     * @return 所有分类列表
     */
    @PostMapping("/queryListCategories")
    @ApiOperation(value = "管理中心获取分类列表", notes = "管理中心获取分类列表", httpMethod = "POST")
    public JsonResult queryListCategories();

    /**
     * 删除分类
     *
     * @param categoryId 分类Id
     * @return yes / no
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除分类", notes = "删除分类")
    public JsonResult deleteCategory(@RequestParam Integer categoryId);

}
