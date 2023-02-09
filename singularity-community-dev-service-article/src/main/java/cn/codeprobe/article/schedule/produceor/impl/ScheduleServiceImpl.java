package cn.codeprobe.article.schedule.produceor.impl;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import cn.codeprobe.api.config.RabbitMq;
import cn.codeprobe.api.config.RabbitMqDelay;
import cn.codeprobe.article.schedule.produceor.ScheduleService;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

/**
 * RabbitMQ 文章定时发布任务
 *
 * @author Lionido
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void produceDownloadHtml(String articleId, String mongoFileId) {
        HashMap<String, String> map = new HashMap<>(5);
        map.put("articleId", articleId);
        map.put("mongoId", mongoFileId);
        String jsonStr = JSONUtil.toJsonStr(map);
        rabbitTemplate.convertAndSend(RabbitMq.EXCHANGE_ARTICLE, "article.download.do", jsonStr);
    }

    @Override
    public void produceDeleteHtml(String articleId) {
        HashMap<String, String> map = new HashMap<>(5);
        map.put("articleId", articleId);
        String jsonStr = JSONUtil.toJsonStr(map);
        rabbitTemplate.convertAndSend(RabbitMq.EXCHANGE_ARTICLE, "article.delete.do", jsonStr);
    }

    @Override
    public void producePublishAppointedArticle(String articleId, Date publishTime) {
        // payload
        HashMap<String, String> map = new HashMap<>(1);
        map.put("articleId", articleId);
        String jsonStr = JSONUtil.toJsonStr(map);
        // 时间差（延迟时间）
        /// int delayTime = (int)(publishTime.getTime() - System.currentTimeMillis());
        long delayTime = DateUtil.betweenMs(new Date(), publishTime);
        // 格式化为易读的时间差(ms级)
        String formatBetween = DateUtil.formatBetween(delayTime, BetweenFormatter.Level.MILLISECOND);
        System.out.println(formatBetween);
        // 延迟消息
        MessagePostProcessor messagePostProcessor = message -> {
            // 设置消息持久
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // 设置消息延迟时间，ms毫秒
            /// message.getMessageProperties().setDelay(delayTime);
            // 测试 30秒
            message.getMessageProperties().setDelay(30 * 1000);
            return message;
        };
        System.out.println("发送延迟消息队列: " + new Date());
        rabbitTemplate.convertAndSend(RabbitMqDelay.EXCHANGE_DELAY, "delay.publish.do", jsonStr, messagePostProcessor);
    }
}
