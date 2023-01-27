package cn.codeprobe.api.threadlocal;

import cn.codeprobe.pojo.po.Admin;
import cn.codeprobe.pojo.po.User;
import cn.hutool.core.util.ObjectUtil;

/**
 * ThreadLocal 异步线程 保存全局 Subject
 *
 * @author Lionido
 */
public class SubjectContext {

    private static final ThreadLocal<User> USER_DO_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<Admin> ADMIN_USER_DO_THREAD_LOCAL = new ThreadLocal<>();

    public SubjectContext() {}

    public static User getUser() {
        return USER_DO_THREAD_LOCAL.get();
    }

    public static void setUser(User user) {
        USER_DO_THREAD_LOCAL.set(user);
    }

    public static boolean checkHasUser() {
        User user = USER_DO_THREAD_LOCAL.get();
        return user != null;
    }

    public static boolean checkHasAdmin() {
        Admin admin = ADMIN_USER_DO_THREAD_LOCAL.get();
        return ObjectUtil.isNotEmpty(admin);
    }

    public static Admin getAdmin() {
        return ADMIN_USER_DO_THREAD_LOCAL.get();
    }

    public static void setAdmin(Admin admin) {
        ADMIN_USER_DO_THREAD_LOCAL.set(admin);
    }

    public static void removeUser() {
        USER_DO_THREAD_LOCAL.remove();
    }

    public static void removeAdmin() {
        ADMIN_USER_DO_THREAD_LOCAL.remove();
    }
}
