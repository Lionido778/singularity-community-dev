package cn.codeprobe.api.threadlocal;

import cn.codeprobe.pojo.po.AdminUserDO;
import cn.codeprobe.pojo.po.AppUserDO;
import cn.hutool.core.util.ObjectUtil;

/**
 * ThreadLocal 异步线程 保存全局 Subject
 *
 * @author Lionido
 */
public class SubjectContext {

    private static final ThreadLocal<AppUserDO> USER_DO_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<AdminUserDO> ADMIN_USER_DO_THREAD_LOCAL = new ThreadLocal<>();

    public SubjectContext() {}

    public static AppUserDO getUser() {
        return USER_DO_THREAD_LOCAL.get();
    }

    public static void setUser(AppUserDO user) {
        USER_DO_THREAD_LOCAL.set(user);
    }

    public static boolean checkHasUser() {
        AppUserDO appUserDO = USER_DO_THREAD_LOCAL.get();
        return appUserDO != null;
    }

    public static boolean checkHasAdmin() {
        AdminUserDO adminUserDO = ADMIN_USER_DO_THREAD_LOCAL.get();
        return ObjectUtil.isNotEmpty(adminUserDO);
    }

    public static AdminUserDO getAdmin() {
        return ADMIN_USER_DO_THREAD_LOCAL.get();
    }

    public static void setAdmin(AdminUserDO admin) {
        ADMIN_USER_DO_THREAD_LOCAL.set(admin);
    }

    public static void removeUser() {
        USER_DO_THREAD_LOCAL.remove();
    }

    public static void removeAdmin() {
        ADMIN_USER_DO_THREAD_LOCAL.remove();
    }
}
