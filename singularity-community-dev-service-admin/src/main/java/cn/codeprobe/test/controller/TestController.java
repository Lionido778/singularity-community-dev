package cn.codeprobe.test.controller;

import cn.codeprobe.api.controller.test.TestAdminControllerApi;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.utils.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lionido
 */
@RestController
public class TestController implements TestAdminControllerApi {

    @Resource
    IdWorker idWorker;

    static final Logger logger = LoggerFactory.getLogger("TestController");


    @Override
    public Object hello() {
        System.out.println(idWorker.nextIdStr());
        return JsonResult.ok("hello! service-admin");
    }

    public static void main(String[] args) {
        String md5DigestAsHex = DigestUtils.md5DigestAsHex("admin".getBytes());
        System.out.println(md5DigestAsHex);
        String hashpw = BCrypt.hashpw(md5DigestAsHex, BCrypt.gensalt());
        System.out.println(hashpw);
    }
}
