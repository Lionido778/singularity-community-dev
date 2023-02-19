package cn.codeprobe.zuul.filters;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.utils.IpUtil;
import cn.codeprobe.utils.RedisUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author Lionido
 */
@Component
@RefreshScope
public class BlackIpFilter extends ZuulFilter {

    @Value("${blackIp.continueCounts}")
    public Integer continueCounts;
    @Value("${blackIp.timeInterval}")
    public Integer timeInterval;
    @Value("${blackIp.limitTimes}")
    public Integer limitTimes;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        System.out.println("执行【ip黑名单】过滤器...");

        System.out.println("continueCounts: " + continueCounts);
        System.out.println("timeInterval: " + timeInterval);
        System.out.println("limitTimes: " + limitTimes);

        // Zuul 获得上下文对象
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        // 获得访问ip
        String ip = IpUtil.getRequestIp(request);

        // 需求： 判断ip在10秒内的请求次数是否超过10次 如果超过，则限制这个ip访问15秒，15秒以后再放行

        final String ipRedisKey = "zuul-ip:" + ip;
        final String ipRedisLimitKey = "zuul-ip-limit:" + ip;

        // 获得当前ip这个key的剩余时间
        long limitLeftTime = redisUtil.ttl(ipRedisLimitKey);
        // 如果当前限制ip的key还存在剩余时间，说明这个ip不能访问，继续等待
        if (limitLeftTime > 0) {
            stopRequest(context);
            return null;
        }

        // 在redis中累加ip的请求访问次数
        long requestCounts = redisUtil.increment(ipRedisKey, 1);
        // 从0开始计算请求次数，初期访问为1，则设置过期时间，也就是连续请求的间隔时间
        if (requestCounts == 1) {
            redisUtil.expire(ipRedisKey, timeInterval);
        }

        // 如果还能取得请求次数，说明用户连续请求的次数落在10秒内
        // 一旦请求次数超过了连续访问的次数，则需要限制这个ip的访问
        if (requestCounts > continueCounts) {
            // 限制ip的访问时间
            redisUtil.set(ipRedisLimitKey, ipRedisLimitKey, limitTimes);
            stopRequest(context);
        }
        return null;
    }

    private void stopRequest(RequestContext context) {
        // 停止zuul继续向下路由，禁止请求通信
        context.setSendZuulResponse(false);
        // 响应状态
        context.setResponseStatusCode(200);
        String result = JSONUtil.toJsonStr(JsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_ZUUL));
        context.setResponseBody(result);
        // 编码
        context.getResponse().setCharacterEncoding("utf-8");
        // application/json
        context.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
