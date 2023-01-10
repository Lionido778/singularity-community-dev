package cn.codeprobe.admin.service.impl;

import cn.codeprobe.admin.service.AdminService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.pojo.AdminUser;
import org.springframework.stereotype.Service;

/**
 * @author Lionido
 */
@Service
public class AdminServiceImpl extends AdminBaseService implements AdminService {
    @Override
    public Boolean checkAdminIsExist(String username) {
        AdminUser adminUser = queryAdminByUsername(username);
        return adminUser != null;
    }
}
