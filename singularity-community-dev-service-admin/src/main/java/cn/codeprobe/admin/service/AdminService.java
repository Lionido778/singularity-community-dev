package cn.codeprobe.admin.service;

import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.result.page.PagedGridResult;

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

    void createAdminUser(NewAdminBO newAdminBO);

    PagedGridResult queryAdminUserList(int page, int pageSize);
}
