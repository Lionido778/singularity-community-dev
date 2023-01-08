package cn.codeprobe.file.test.controller;

import cn.codeprobe.api.controller.test.TestFileControllerApi;
import cn.codeprobe.result.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lionido
 */
@RestController
public class TestController implements TestFileControllerApi {

    static final Logger logger = LoggerFactory.getLogger("TestController");

    @Override
    public Object hello() {
        return JsonResult.ok("service-file: hello!");
    }
}
