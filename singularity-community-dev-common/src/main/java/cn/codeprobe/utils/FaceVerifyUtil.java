package cn.codeprobe.utils;


import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.utils.extend.AliyunResource;
import com.aliyun.facebody20191230.models.CompareFaceResponse;
import com.aliyun.facebody20191230.models.CompareFaceResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author Lionido
 */
@Component
public class FaceVerifyUtil {

    @Resource
    private AliyunResource aliyunResource;

    /**
     * 引入依赖包
     * <dependency>
     * <groupId>com.aliyun</groupId>
     * <artifactId>facebody20191230</artifactId>
     * <version>${aliyun.facebody.version}</version>
     * </dependency>
     */

    public static com.aliyun.facebody20191230.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "facebody.cn-shanghai.aliyuncs.com";
        return new com.aliyun.facebody20191230.Client(config);
    }

    public Boolean verifyFace(Float targetConfidence, String faceFilePath, String dataFilePath) throws Exception {
        // "YOUR_ACCESS_KEY_ID", "YOUR_ACCESS_KEY_SECRET" 的生成请参考https://help.aliyun.com/document_detail/175144.html
        // 如果您是用的子账号AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参考https://help.aliyun.com/document_detail/145025.html
        com.aliyun.facebody20191230.Client client = FaceVerifyUtil.createClient(aliyunResource.getAccessKeyId(), aliyunResource.getAccessKeySecret());
        // 场景一，使用本地文件
        InputStream inputStreamA = Files.newInputStream(new File(faceFilePath).toPath());
        InputStream inputStreamB = Files.newInputStream(new File(dataFilePath).toPath());
        // 场景二，使用任意可访问的url
        // URL url = new URL("https://viapi-test-bj.oss-cn-beijing.aliyuncs.com/viapi-3.0domepic/facebody/CompareFace/CompareFace-left1.png");
        com.aliyun.facebody20191230.models.CompareFaceAdvanceRequest compareFaceAdvanceRequest = new com.aliyun.facebody20191230.models.CompareFaceAdvanceRequest()
                .setImageURLAObject(inputStreamA)
                .setImageURLBObject(inputStreamB);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        Float confidence = (float) 0;
        try {
            // 复制代码运行请自行打印 API 的返回值
            CompareFaceResponse compareFaceResponse = client.compareFaceAdvance(compareFaceAdvanceRequest, runtime);
            CompareFaceResponseBody body = compareFaceResponse.getBody();
            // 获取 匹配度
            confidence = body.getData().getConfidence();
            System.out.println("人脸匹配度：" + confidence);
            // 获取整体结果
            System.out.println(com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(compareFaceResponse)));
        } catch (TeaException teaException) {
            // 获取整体报错信息
            System.out.println(com.aliyun.teautil.Common.toJSONString(teaException));
            // 获取单个字段
            System.out.println(teaException.getCode());
            GlobalExceptionManage.internal(ResponseStatusEnum.FACE_VERIFY_LOGIN_ERROR);
        } finally {
            inputStreamA.close();
            inputStreamB.close();
        }
        return confidence > targetConfidence;
    }
}

