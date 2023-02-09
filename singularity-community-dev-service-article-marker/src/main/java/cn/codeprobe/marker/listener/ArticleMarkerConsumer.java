package cn.codeprobe.marker.listener;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import cn.codeprobe.api.config.RabbitMq;
import cn.codeprobe.enums.RabbitMQ;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.marker.service.MarkerService;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

/**
 * 文章消费端: 监听消息队列
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
            if (RabbitMQ.MQ_DOWNLOAD.value.equalsIgnoreCase(routingKey)) {
                Map map = JSONUtil.toBean(payload, Map.class);
                String articleId = (String)map.get("articleId");
                String mongoId = (String)map.get("mongoId");
                markerService.downloadHtml(articleId, mongoId);
            } else if (RabbitMQ.MQ_DELETE.value.equalsIgnoreCase(routingKey)) {
                Map map = JSONUtil.toBean(payload, Map.class);
                String articleId = (String)map.get("articleId");
                markerService.deleteHtml(articleId);
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.RABBITMQ_ERROR);
        }
    }
}
