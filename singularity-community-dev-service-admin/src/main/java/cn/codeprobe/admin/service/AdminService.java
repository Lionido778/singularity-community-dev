package cn.codeprobe.admin.service;

import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.result.page.PagedGridResult;

/**
 * @author Lionido
 */
public interface AdminService {

    /**
     * 通过管理员登录名检查管理员是否在存在
     *
     * @param adminName 管理员登录名
     * @return true exist;false: no exist
     */
    public Boolean checkAdminIsExist(String adminName);

    /**
     * 创建管理员
     *
     * @param newAdminBO 管理员表单数据
     */
    void saveAdminUser(NewAdminBO newAdminBO);

    /**
     * 分页获取所有管理员账户
     *
     * @param page     当前页
     * @param pageSize 当前查询页记录数
     * @return 分页结果
     */
    PagedGridResult pageListAdminUsers(int page, int pageSize);
}
