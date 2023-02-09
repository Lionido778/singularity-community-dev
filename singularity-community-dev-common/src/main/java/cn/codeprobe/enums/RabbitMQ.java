package cn.codeprobe.enums;

/**
 * @author Lionido
 */

public enum RabbitMQ {

    /**
     * routeKey
     */
    DELAY_PUBLISH("delay.publish.do"),
    MQ_DOWNLOAD("article.download.do"),
    MQ_DELETE("article.delete.do");

    public final String value;

    RabbitMQ(String value) {
        this.value = value;
    }

}
