package cn.codeprobe.api.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.result.JsonResult;

/**
 * 前后端分离了模式下，重写 /error 的请求
 *
 * @author Lionido
 */
@Controller
public class NotFoundController extends AbstractErrorController {

    public NotFoundController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * 在springboot2.3.0新增了server.error.path进行配置,这个废弃使用了，之前版本可以直接通过设置这个返回值修改默认/error的路径
     */

    @Override
    public String getErrorPath() {
        return null;
    }

    /**
     * 默认路径/error，可以通过server.error.path配置
     */
    @ResponseBody
    @RequestMapping(("${server.error.path:/error}"))
    public JsonResult notFoundError(HttpServletRequest request, HttpServletResponse response) {
        // response.setStatus(HttpStatus.OK.value()); 前端要求 HttpStatus 设置为200，不能设置为404
        return JsonResult.errorCustom(ResponseStatusEnum.NOT_FOUND_ERROR);
    }
}
