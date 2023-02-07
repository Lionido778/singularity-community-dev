package cn.codeprobe.marker.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import cn.codeprobe.api.config.RabbitMq;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.marker.service.MarkerService;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

/**
 * MQ 文章消费端
 * 
 * @author Lionido
 */
@Component
public class ArticleMarkerConsumer {

    @Resource
    private MarkerService markerService;

    @RabbitListener(queues = {RabbitMq.QUEUE_DOWNLOAD_HTML})
    public void watch(String payload, Message message) {
        System.out.println(payload);
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if (CharSequenceUtil.isNotBlank(routingKey)) {
            if ("article.download.do".equalsIgnoreCase(routingKey)) {
                Map map = JSONUtil.toBean(payload, Map.class);
                String articleId = (String)map.get("articleId");
                String mongoId = (String)map.get("mongoId");
                markerService.publishHtmlByMq(articleId, mongoId);
            } else if ("article.delete.do".equalsIgnoreCase(routingKey)) {
                Map map = JSONUtil.toBean(payload, Map.class);
                String articleId = (String)map.get("articleId");
                markerService.deleteHtmlByMq(articleId);
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.RABBITMQ_ERROR);
        }
    }
}
