package cn.codeprobe.api.controller.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;

/**
 * 这是一个基础 Controller 目的：提供一些常量和属性，方便解耦
 *
 * @author Lionido
 */
public class ApiController {

    @Resource
    public HttpServletRequest request;
    @Resource
    public HttpServletResponse response;

    /// **
    // * 获取访问主体 Subject(本系统访问者主要是：用户、管理员) 的ID
    // * @return UserID 或 AdminId
    // */
    // public String getSubjectIdFromHeader() {
    // request.getHeader()
    // return null;
    // }

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
            // 对cookie进行编码，解决cookies存放中文，导致前台乱码
            String encodeCookieValue = URLEncoder.encode(cookieValue, "UTF-8");
            System.out.println(cookieName + ":" + cookieValue + "-" + encodeCookieValue);
            // 生成cookie
            createCookie(cookieName, encodeCookieValue, maxAge, domainName);
        } catch (UnsupportedEncodingException e) {
            GlobalExceptionManage.internal(ResponseStatusEnum.USER_LOGIN_FAILED_COOKIE_ERROR);
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
