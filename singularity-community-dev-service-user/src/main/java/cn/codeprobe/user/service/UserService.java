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
    public AppUser queryAppUserIsExistByMobile(String mobile);

    /**
     * 注册用户
     */
    public AppUser createAppUser(String mobile);

    /**
     * 用户注册登录
     */
    public AppUser RegisterLogin(RegisterLoginBO registerLoginBO);

    /**
     * 通过主键查询用户
     */
    public AppUser queryUserById(String userId);

    /**
     * 更新用户信息
     */
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);
}
