package cn.codeprobe.admin.controller.category;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.admin.service.category.CategoryWriterService;
import cn.codeprobe.api.controller.admin.category.CategoryWriterControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.result.JsonResult;

/**
 * @author Lionido
 */
@RestController
public class CategoryWriterController extends ApiController implements CategoryWriterControllerApi {

    @Resource
    private CategoryWriterService categoryWriterService;

    @Override
    public JsonResult queryCategories() {
        return JsonResult.ok(categoryWriterService.getCategories());
    }

}
