package cn.codeprobe.test.controller;


import cn.codeprobe.api.controller.test.RedisControllerApi;
import cn.codeprobe.result.JSONResult;
import cn.codeprobe.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class RedisController implements RedisControllerApi {


    static final Logger logger = LoggerFactory.getLogger("RedisController");

    @Resource
    private RedisUtil redisUtil;


    @Override
    public JSONResult redis() {
        redisUtil.set("test", "测试值");
        return JSONResult.ok(redisUtil.get("test"));
    }
}
