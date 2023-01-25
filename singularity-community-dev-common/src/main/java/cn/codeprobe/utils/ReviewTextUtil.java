package cn.codeprobe.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.aliyun.imageaudit20191230.models.ScanTextRequest;
import com.aliyun.imageaudit20191230.models.ScanTextResponse;
import com.aliyun.imageaudit20191230.models.ScanTextResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaModel;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.utils.extend.AliyunResource;

/**
 * 引入依赖包 <dependency> <groupId>com.aliyun</groupId> <artifactId>imageaudit20191230</artifactId>
 * <version>${aliyun.imageaudit.version}</version> </dependency>
 * <p>
 * 阿里云AI文本内容审核
 *
 * @author Lionido
 */
@Component
public class ReviewTextUtil {

    private static final String TEXT_ABUSE_LABEL = "abuse";
    @Resource
    private AliyunResource aliyunResource;

    public com.aliyun.imageaudit20191230.Client createClient(String accessKeyId, String accessKeySecret)
        throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
            // 必填，您的 AccessKey ID
            .setAccessKeyId(accessKeyId)
            // 必填，您的 AccessKey Secret
            .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "imageaudit.cn-shanghai.aliyuncs.com";
        return new com.aliyun.imageaudit20191230.Client(config);
    }

    public Map<String, String> scanText(String content) {
        // "YOUR_ACCESS_KEY_ID", "YOUR_ACCESS_KEY_SECRET" 的生成请参见：https://help.aliyun.com/document_detail/175144.html
        // 如果您是用的子账号AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参见：https://help.aliyun.com/document_detail/145025.html
        com.aliyun.imageaudit20191230.Client client = null;
        try {
            client = createClient(aliyunResource.getAccessKeyId(), aliyunResource.getAccessKeySecret());
        } catch (Exception e) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        // content
        ScanTextRequest.ScanTextRequestTasks tasks = new ScanTextRequest.ScanTextRequestTasks().setContent(content);
        // label
        ScanTextRequest.ScanTextRequestLabels labels = new ScanTextRequest.ScanTextRequestLabels();
        labels.setLabel(TEXT_ABUSE_LABEL);

        com.aliyun.imageaudit20191230.models.ScanTextRequest scanTextRequest =
            new com.aliyun.imageaudit20191230.models.ScanTextRequest().setLabels(Collections.singletonList(labels))
                .setTasks(Collections.singletonList(tasks));
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();

        ScanTextResponse response = null;
        try {
            // 复制代码运行请自行打印 API 的返回值
            response = client.scanTextWithOptions(scanTextRequest, runtime);
            System.out.println(com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(response)));
        } catch (TeaException error) {
            // 获取整体报错信息
            System.out.println(com.aliyun.teautil.Common.toJSONString(error));
            // 获取单个字段
            System.out.println(error.getCode());
        } catch (Exception e) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        // Suggestion Map
        Map<String, String> map = new HashMap<>(0);
        for (ScanTextResponseBody.ScanTextResponseBodyDataElements element : response.getBody().getData()
            .getElements()) {
            for (ScanTextResponseBody.ScanTextResponseBodyDataElementsResults result : element.getResults()) {
                String suggestion = result.getSuggestion();
                String label = result.getLabel();
                map.put(label, suggestion);
            }
        }
        return map;
    }
}
