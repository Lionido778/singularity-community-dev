package cn.codeprobe.admin.service.base;

import cn.codeprobe.admin.mapper.AdminUserMapper;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.pojo.AdminUser;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.utils.RedisUtil;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @author Lionido
 */
public class AdminBaseService extends ApiController {

    @Resource
    public AdminUserMapper adminUserMapper;
    @Resource
    public Sid sid;
    @Resource
    public RedisUtil redisUtil;

    /**
     * domain-name
     */
    @Value("${website.domain-name}")
    public String domainName;

    /**
     * token
     */
    public static final String REDIS_ADMIN_TOKEN = "admin_token";
    public static final String REDIS_ADMIN_INFO = "admin_info";
    public static final Integer REDIS_ADMIN_TOKEN_TIMEOUT = 30 * 60;

    /**
     * cookie
     */
    public static final String COOKIE_NAME_ADMIN_ID = "aid";
    public static final String COOKIE_NAME_ADMIN_TOKEN = "atoken";
    public static final String COOKIE_NAME_ADMIN_NAME = "aname";
    public static final Integer COOKIE_ADMIN_MAX_AGE = 24 * 60 * 60;
    public static final Integer COOKIE_TIME_OUT = 0;


    /**
     * 获取管理员用户信息
     */
    public AdminUser queryAdminByUsername(String username) {
        Example example = new Example(AdminUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return adminUserMapper.selectOneByExample(example);
    }

    /**
     * 密码加密
     */
    public String encryptPassword(String password) {
        // 先将密码进行MD5加密
        String md5Pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        // 后将MD5加密后的密文进行加盐加密
        return BCrypt.hashpw(md5Pwd, BCrypt.gensalt());
    }

    /**
     * 密码 解密
     */
    public Boolean isPasswordMatched(String plainText, String encrypted) {
        // 先将密码进行MD5加密
        String md5Pwd = DigestUtils.md5DigestAsHex(plainText.getBytes());
        return BCrypt.checkpw(md5Pwd, encrypted);
    }

    /**
     * admin 登录后 生成 token、cookie
     */
    public void adminLoginSetting(AdminUser adminUser) {
        String adminId = adminUser.getId();
        UUID uuid = UUID.randomUUID();
        String adminToken = uuid.toString();
        String adminName = adminUser.getUsername();
        // 将adminToken写入redis
        redisUtil.set(REDIS_ADMIN_TOKEN + ":" + adminId, adminToken, REDIS_ADMIN_TOKEN_TIMEOUT);
        // 设置cookie
        setCookie(COOKIE_NAME_ADMIN_ID, adminId, COOKIE_ADMIN_MAX_AGE, domainName);
        setCookie(COOKIE_NAME_ADMIN_TOKEN, adminToken, COOKIE_ADMIN_MAX_AGE, domainName);
        setCookie(COOKIE_NAME_ADMIN_NAME, adminName, COOKIE_ADMIN_MAX_AGE, domainName);
    }

    public PagedGridResult setterPageGrid(List<?> list, int page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setTotal(pageInfo.getPages());
        gridResult.setRecords(pageInfo.getTotal());
        return gridResult;
    }

}
