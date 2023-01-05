package cn.codeprobe.test.controller;

import cn.codeprobe.api.controller.test.TestControllerApi;
import cn.codeprobe.result.JSONResult;
import cn.codeprobe.utils.RedisUtil;
import cn.codeprobe.utils.SMSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController implements TestControllerApi {

    static final Logger logger = LoggerFactory.getLogger("HelloController");

    @Override
    public Object Hello() {
        logger.debug("debug:" + "hello");
        logger.info("info:" + "hello");
        logger.warn("warn:" + "hello");
        logger.error("error:" + "hello");

        return JSONResult.ok();
//        return JSONResult.error();
//        return JSONResult.errorMsg("自定义错误消息");
//        return JSONResult.errorTicket();
    }

    @Resource
    private RedisUtil redisUtil;

    @Override
    public JSONResult redis() {
        redisUtil.set("test", "测试值");
        return JSONResult.ok(redisUtil.get("test"));
    }

    @Resource
    private SMSUtil smsUtil;

    @Override
    public Object sendSms() throws Exception {
        smsUtil.sendSms("17789445253","123123");
        return null;
    }

}
