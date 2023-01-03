package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.user.HelloControllerApi;
import cn.codeprobe.result.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    static final Logger logger = LoggerFactory.getLogger("HelloController");

    @Override
    public Object Hello() {
        logger.debug("debug:" + "hello");
        logger.info("info:" + "hello");
        logger.warn("warn:" + "hello");
        logger.error("error:" + "hello");

//        return JSONResult.ok();
//        return JSONResult.error();
//        return JSONResult.errorMsg("自定义错误消息");
        return JSONResult.errorTicket();
    }

}
