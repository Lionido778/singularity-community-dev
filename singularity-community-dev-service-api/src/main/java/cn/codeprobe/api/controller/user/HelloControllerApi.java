package cn.codeprobe.api.controller.user;

import org.springframework.web.bind.annotation.GetMapping;

public interface HelloControllerApi {

    @GetMapping("/helloapi")
    Object Hello();
}
