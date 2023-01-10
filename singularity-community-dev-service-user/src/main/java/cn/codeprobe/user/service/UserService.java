package cn.codeprobe.user.service;


import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;

public interface UserService {

    ///**
    // * 获取短信验证码
    // */
    //public void getSMSCode(String mobile);

    /**
     * 根据手机号查询用户是否存在
     */
    public AppUser queryAppUserIsExist(String mobile);

    /**
     * 注册用户
     */
    public AppUser createAppUser(String mobile);

    ///**
    // * 用户注册登录
    // */
    //public AppUser registerLogin(RegisterLoginBO registerLoginBO);

    /**
     * 获取用户 (缓存,数据库)
     * 由于该接口调用量巨大，为了缓解数据库压力，设计了缓存
     */
    public AppUser getUserInfo(String userId);

    /**
     * 更新用户账户信息
     */
    public void updateUserAccountInfo(UpdateUserInfoBO updateUserInfoBO);

    ///**
    // * 用户退出登录
    // */
    //void userLogout(String userId);
}
