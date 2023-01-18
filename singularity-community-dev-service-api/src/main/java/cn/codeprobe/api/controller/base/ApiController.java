package cn.codeprobe.api.controller.base;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这是一个基础 Controller
 * 目的：提供一些常量和属性，方便解耦
 *
 * @author Lionido
 */
public class ApiController {


    @Resource
    public HttpServletRequest request;
    @Resource
    public HttpServletResponse response;


    /**
     * 获取前端数据校验的错误信息
     */
    public Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>(0);
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            map.put(field, message);
        }
        return map;
    }


    /**
     * 登录成功后设置 cookie
     */
    public void setCookie(String cookieName, String cookieValue, Integer maxAge) {
        setCookie(cookieName, cookieValue, maxAge, "");
    }

    /**
     * 登录成功后设置 cookie (domainName)
     */
    public void setCookie(String cookieName, String cookieValue, Integer maxAge, String domainName) {
        try {
            String encodeCookieValue = URLEncoder.encode(cookieValue, "UTF-8");
            System.out.println(cookieName + ":" + cookieValue + "-" + encodeCookieValue);
            // 生成cookie
            createCookie(cookieName, encodeCookieValue, maxAge, domainName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成 Cookie
     */
    public void createCookie(String cookieName, String cookieValue, Integer maxAge, String domainName) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setDomain(domainName);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
