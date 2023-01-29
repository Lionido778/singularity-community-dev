package cn.codeprobe.admin.controller.category;

import java.util.Map;

import javax.annotation.Resource;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.admin.service.category.CategoryMngService;
import cn.codeprobe.api.controller.admin.category.CategoryMngControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.pojo.bo.NewCategoryBO;
import cn.codeprobe.result.JsonResult;

/**
 * @author Lionido
 */
@RestController
public class CategoryMngController extends ApiController implements CategoryMngControllerApi {

    @Resource
    private CategoryMngService categoryMngService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult addOrModifyCategory(NewCategoryBO newCategoryBO, @NotNull BindingResult result) {
        // 校验 BO 数据
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return JsonResult.errorMap(map);
        }
        // 调用 service 保存、更新分类
        categoryMngService.saveOrUpdateCategory(newCategoryBO);
        return JsonResult.ok();
    }

    @Override
    public JsonResult queryListCategories() {
        return JsonResult.ok(categoryMngService.listCategories());
    }

    @Override
    public JsonResult deleteCategory(Integer categoryId) {
        categoryMngService.removeCategory(categoryId);
        return JsonResult.ok();
    }
}
