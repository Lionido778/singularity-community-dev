package cn.codeprobe.admin.service;

/**
 * @author Lionido
 */
public interface AdminService {

    /**
     * 通过管理员登录名检查管理员是否在存在
     * @param adminName 管理员登录名
     * @return true exist;false: no exist
     */
    public Boolean checkAdminIsExist(String adminName);
}
