package cn.codeprobe.admin.controller.category;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.admin.service.category.CategoryPortalService;
import cn.codeprobe.api.controller.admin.category.CategoryPortalControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.result.JsonResult;

/**
 * @author Lionido
 */
@RestController
public class CategoryPortalController extends ApiController implements CategoryPortalControllerApi {

    @Resource
    private CategoryPortalService categoryPortalService;

    @Override
    public JsonResult queryCategories() {
        return JsonResult.ok(categoryPortalService.getCategories());
    }

}
