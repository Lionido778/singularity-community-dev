package cn.codeprobe.admin.service;

/**
 * @author Lionido
 */
public interface AdminPassportService {

    /**
     * 管理员用户密码登录
     *
     * @param username 登录名
     * @param password 密码
     */
    void loginByUsernameAndPwd(String username, String password);

    /**
     * 人脸识别登录
     *
     * @param username  用户名
     * @param img64Face 人脸登录图片
     */
    void loginByFace(String username, String img64Face);

    /**
     * 管理员退出登录
     *
     * @param adminId 管理员ID
     */
    void adminLogout(String adminId);
}
