package cn.codeprobe.user.service;


import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;

public interface UserService {

    public void getSMSCode(String mobile);

    public AppUser queryAppUserIsExistByMobile(String mobile);

    public AppUser createAppUser(String mobile);

    public AppUser RegisterLogin(RegisterLoginBO registerLoginBO);

    public AppUser queryUserById(String userId);
}
