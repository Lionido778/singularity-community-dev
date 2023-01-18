package cn.codeprobe.article.controller.test;

import cn.codeprobe.api.controller.test.TestArticleControllerApi;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.utils.IdWorker;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lionido
 */
@RestController
public class TestController implements TestArticleControllerApi {

    @Resource
    private IdWorker idWorker;

    @Override
    public Object hello() {
        String id1 = idWorker.nextIdStr();
        Long id2 = idWorker.nextId();
        long workerId = idWorker.getSnowflake().getWorkerId(id2);
        long centerId = idWorker.getSnowflake().getDataCenterId(id2);
        long time = idWorker.getSnowflake().getGenerateDateTime(id2);
        DateTime date = DateUtil.date(time);
        System.out.println(date);
        System.out.println(id1);
        System.out.println(id2);
        System.out.println(workerId);
        System.out.println(centerId);
        System.out.println(time);
        return JsonResult.ok();
    }

}
