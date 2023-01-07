package cn.codeprobe.user.service.impl;

import cn.codeprobe.api.controller.base.BaseController;
import cn.codeprobe.enums.UserSex;
import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.user.mapper.AppUserMapper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BaseService extends BaseController {

    @Resource
    public AppUserMapper appUserMapper;
    @Resource
    public Sid sid;

    // user
    public final String NICKNAME_PREFIX = "用户_";
    public final Integer USER_UN_ACTIVE = UserStatus.INACTIVE.type;
    public final Integer USER_ACTIVE = UserStatus.ACTIVE.type;
    public final Integer USER_FROZEN = UserStatus.FROZEN.type;
    public final String USER_BIRTHDAY = "1900-1-1";
    public final Integer USER_SEX = UserSex.secret.type;
    public final Integer USER_TOTAL_INCOME = 0;
    public final String USER_FACE = "https://img2.baidu.com/it/u=132501275,3612619305&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";

    // token & cookie
    public final String REDIS_USER_TOKEN = "user_token";
    public final Integer TOKEN_TIMEOUT = 60 * 60;
    public final String COOKIE_NAME_ID = "uid";
    public final String COOKIE_NAME_TOKEN = "utoken";
    public final Integer COOKIE_MAX_AGE = 7 * 24 * 60 * 60;
    public final String REDIS_USER_INFO = "user_info";


    //  domain-name
    @Value("${website.domain-name}")
    private String DOMAIN_NAME;


    public void setCookie(String cookieName, String cookieValue, Integer maxAge) {
        try {
            String encodeCookieValue = URLEncoder.encode(cookieValue, "utf-8");
            // 生成cookie
            createCookie(cookieName, encodeCookieValue, maxAge);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void createCookie(String cookieName, String cookieValue, Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setDomain(DOMAIN_NAME);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    public AppUser getUser(String userId) {
        return appUserMapper.selectByPrimaryKey(userId);
    }

}
