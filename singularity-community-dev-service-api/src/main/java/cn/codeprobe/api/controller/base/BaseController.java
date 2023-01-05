package cn.codeprobe.api.controller.base;

import cn.codeprobe.utils.RedisUtil;
import cn.codeprobe.utils.SMSUtil;

import javax.annotation.Resource;

/**
 * 这是一个基础 Controller
 * 目的：提供一些常量和属性，方便解耦
 */
public class BaseController {

    @Resource
    public SMSUtil smsUtil;

    @Resource
    public RedisUtil redisUtil;

    public final static String MOBILE_SMSCODE = "mobile:smscode";
    public final static Long MOBILE_SMSCODE_TIMEOUT = (long) (30 * 60);
}
