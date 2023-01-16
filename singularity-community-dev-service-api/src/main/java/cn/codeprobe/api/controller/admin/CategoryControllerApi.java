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
@RequestMapping("/adminMng/categoryMng")
public interface CategoryControllerApi {

    @PostMapping("/saveOrUpdateCategory")
    @ApiOperation(value = "新增或更新分类", notes = "新增或更新分类")
    public JsonResult saveOrUpdateFriendLink(@RequestBody @Valid CategoryBO categoryBO, BindingResult result);

    @PostMapping("/getCategoryList")
    @ApiOperation(value = "管理中心获取分类列表", notes = "管理中心获取分类列表", httpMethod = "POST")
    public JsonResult getCategoryList();

    @GetMapping("/getCategories")
    @ApiOperation(value = "创作中心获取分类列表", notes = "创作中心获取分类列表", httpMethod = "GET")
    public JsonResult getCategories();

    @PostMapping("/delete")
    @ApiOperation(value = "删除分类", notes = "删除分类")
    public JsonResult deleteCategory(@RequestParam Integer categoryId);

}



