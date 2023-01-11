package cn.codeprobe.admin.service.impl;

import cn.codeprobe.admin.service.AdminPassportService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.AdminUser;
import org.springframework.stereotype.Service;

/**
 * @author Lionido
 */
@Service
public class AdminPassportServiceImpl extends AdminBaseService implements AdminPassportService {

    @Override
    public void loginByUsernameAndPwd(String userName, String password) {
        AdminUser adminUser = queryAdminByUsername(userName);
        if (adminUser != null) {
            String encryptedPwd = adminUser.getPassword();
            if (Boolean.TRUE.equals(isPasswordMatched(password, encryptedPwd))) {
                // 登陆成功，进行 token、cookie配置
                adminLoginSetting(adminUser);
            } else {
                GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    @Override
    public void adminLogout(String adminId) {
        // 删除 redis 中的token
        redisUtil.del(REDIS_ADMIN_TOKEN + ":" + adminId);
        // 删除 cookie
        setCookie(COOKIE_NAME_ADMIN_ID, "", COOKIE_TIME_OUT, domainName);
        setCookie(COOKIE_NAME_ADMIN_TOKEN, "", COOKIE_TIME_OUT, domainName);
        setCookie(COOKIE_NAME_ADMIN_NAME, "", COOKIE_TIME_OUT, domainName);
    }
}
