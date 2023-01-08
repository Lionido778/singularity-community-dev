package cn.codeprobe.utils.extend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 将 aliyun.properties 以Java对象（AliyunResource）的形式进行操作
 *
 * @author Lionido
 */
@Component
@PropertySource("classpath:aliyun.properties")
@ConfigurationProperties(prefix = "sms")
public class AliyunResource {

    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateCode;

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
