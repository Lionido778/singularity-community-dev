package cn.codeprobe.article.controller.test;

import cn.codeprobe.api.controller.test.TestArticleControllerApi;
import cn.codeprobe.result.JsonResult;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lionido
 */
@RestController
public class TestController implements TestArticleControllerApi {

    @Override
    public Object hello() {
        return JsonResult.ok("hello article!");
    }

}
