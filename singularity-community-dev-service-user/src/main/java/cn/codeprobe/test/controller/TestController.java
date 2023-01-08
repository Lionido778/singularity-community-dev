package cn.codeprobe.test.controller;

import cn.codeprobe.api.controller.test.TestUserControllerApi;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.utils.RedisUtil;
import cn.codeprobe.utils.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lionido
 */
@RestController
public class TestController implements TestUserControllerApi {

    static final Logger logger = LoggerFactory.getLogger("TestController");

    @Override
    public Object hello() {
        logger.debug("debug:" + "hello1");
        logger.info("info:" + "hello2");
        logger.warn("warn:" + "hello3");
        logger.error("error:" + "hello4");

        return JsonResult.ok();
    }

    @Resource
    private RedisUtil redisUtil;

    @Override
    public JsonResult redis() {
        redisUtil.set("test", "测试值");
        return JsonResult.ok(redisUtil.get("test"));
    }

    @Resource
    private SmsUtil smsUtil;

    @Override
    public Object sendSms() throws Exception {
        smsUtil.sendSms("17789445253","123123");
        return null;
    }

}
