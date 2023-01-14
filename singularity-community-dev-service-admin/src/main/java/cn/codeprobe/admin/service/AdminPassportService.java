package cn.codeprobe.admin.service;

/**
 * @author Lionido
 */
public interface AdminPassportService {

    void loginByUsernameAndPwd(String username, String password);

    void loginByFace(String username, String img64Face);
    void adminLogout(String adminId);
}
