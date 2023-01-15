package cn.codeprobe.api.controller.admin;


import cn.codeprobe.pojo.bo.CategoryBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @ApiOperation(value = "获取分类列表", notes = "获取分类列表")
    public JsonResult getFriendLinkList();

    @PostMapping("/delete")
    @ApiOperation(value = "删除分类", notes = "删除分类")
    public JsonResult deleteCategory(@RequestParam Integer categoryId);

}



