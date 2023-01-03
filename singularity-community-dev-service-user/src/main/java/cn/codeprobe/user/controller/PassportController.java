package cn.codeprobe.user.controller;

import cn.codeprobe.api.controller.user.HelloControllerApi;
import cn.codeprobe.api.controller.user.PassportControllerApi;
import cn.codeprobe.result.JSONResult;
import cn.codeprobe.utils.SMSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class PassportController implements PassportControllerApi {

    static final Logger logger = LoggerFactory.getLogger("HelloController");

@Resource
private SMSUtil smsUtil;

    @Override
    public Object sendSms() throws Exception {
        smsUtil.sendSms("17789445253","123123");
        return null;
    }
}
