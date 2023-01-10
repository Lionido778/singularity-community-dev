package cn.codeprobe.user.service;

import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;

/**
 * @author Lionido
 */
public interface UserPassportService {
    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     */
    public void getSmsCode(String mobile);

    /**
     * 用户注册登录
     *
     * @param registerLoginBO 注册登陆表单
     * @return AppUser
     */
    public AppUser registerLogin(RegisterLoginBO registerLoginBO);

    /**
     * 用户退出登录
     *
     * @param userId 用户ID
     */
    void userLogout(String userId);
}
