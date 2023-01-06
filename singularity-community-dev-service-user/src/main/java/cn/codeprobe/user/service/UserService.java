package cn.codeprobe.user.service;


import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;

public interface UserService {

    /**
     * 获取短信验证码
     */
    public void getSMSCode(String mobile);

    /**
     * 根据手机号查询用户是否存在
     */
    public AppUser queryAppUserIsExist(String mobile);

    /**
     * 注册用户
     */
    public AppUser createAppUser(String mobile);

    /**
     * 用户注册登录
     */
    public AppUser RegisterLogin(RegisterLoginBO registerLoginBO);

    /**
     * 获取用户信息
     */
    public AppUser getUserAccountInfo(String userId);

    /**
     * 更新用户账户信息
     */
    public void updateUserAccountInfo(UpdateUserInfoBO updateUserInfoBO);
}
