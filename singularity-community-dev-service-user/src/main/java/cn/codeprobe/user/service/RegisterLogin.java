package cn.codeprobe.user.service;


import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;

public interface RegisterLogin {

    public AppUser queryAppUserByMobile(String mobile);

    public AppUser createAppUser(String mobile);

    public AppUser RegisterLogin(RegisterLoginBO registerLoginBO);
}
