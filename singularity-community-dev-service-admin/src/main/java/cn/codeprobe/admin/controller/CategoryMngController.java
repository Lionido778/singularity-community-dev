package cn.codeprobe.admin.controller;

import cn.codeprobe.admin.service.CategoryService;
import cn.codeprobe.api.controller.admin.CategoryMngControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.pojo.bo.CategoryBO;
import cn.codeprobe.result.JsonResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Lionido
 */
@RestController
public class CategoryMngController extends ApiController implements CategoryMngControllerApi {

    @Resource
    private CategoryService categoryService;

    @Override
    public JsonResult addOrModifyCategory(CategoryBO categoryBO, BindingResult result) {
        // 校验 BO 数据
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return JsonResult.errorMap(map);
        }
        // 调用 service 保存、更新分类
        categoryService.saveOrUpdateCategory(categoryBO);
        return JsonResult.ok();
    }

    @Override
    public JsonResult queryListCategories() {
        return JsonResult.ok(categoryService.listCategories());
    }

    @Override
    public JsonResult queryCategories() {
        return JsonResult.ok(categoryService.getCategories());
    }

    @Override
    public JsonResult deleteCategory(Integer categoryId) {
        categoryService.removeCategory(categoryId);
        return JsonResult.ok();
    }
}
