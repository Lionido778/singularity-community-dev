package cn.codeprobe.admin.service.base;

import cn.codeprobe.admin.mapper.AdminUserMapper;
import cn.codeprobe.admin.mapper.CategoryMapper;
import cn.codeprobe.admin.repository.FriendLinkRepository;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.pojo.po.AdminUserDO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.utils.FaceVerifyUtil;
import cn.codeprobe.utils.IdWorker;
import cn.codeprobe.utils.RedisUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @author Lionido
 */
public class AdminBaseService extends ApiController {

    /**
     * token
     */
    public static final String REDIS_ADMIN_TOKEN = "admin_token";
    public static final String REDIS_ADMIN_INFO = "admin_info";
    public static final String REDIS_ALL_CATEGORIES = "redis_all_categories";
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
     * 人脸识别
     */
    public static final Float TARGET_CONFIDENCE = 80F;
    public static final String FACE_TEMP_DIR = "/workspace/compare";
    public static final String LOGIN_FACE_NAME = "登陆头像";
    public static final String FACE_DATA_NAME = "人脸数据";
    public static final String EXTEND_NAME = ".png";
    /**
     * file url
     */
    public static final String FILE_SERVER_URL = "http://file.codeprobe.cn:8004/file/readBase64FromGridFS?faceId=";
    @Resource
    public AdminUserMapper adminUserMapper;
    @Resource
    public IdWorker idWorker;
    @Resource
    public RedisUtil redisUtil;
    @Resource
    public RestTemplate restTemplate;
    @Resource
    public FaceVerifyUtil faceVerifyUtil;
    @Resource
    public FriendLinkRepository friendLinkRepository;
    @Resource
    public CategoryMapper categoryMapper;
    /**
     * domain-name
     */
    @Value("${website.domain-name}")
    public String domainName;

    /**
     * 获取管理员用户信息
     */
    public AdminUserDO queryAdminByUsername(String username) {
        Example example = new Example(AdminUserDO.class);
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
     * admin 登录后成功后 生成token、cookie
     */
    public void adminLoggedSetting(AdminUserDO adminUserDO) {
        String adminId = adminUserDO.getId();
        UUID uuid = UUID.randomUUID();
        String adminToken = uuid.toString();
        String adminName = adminUserDO.getUsername();
        // 将adminToken写入redis
        redisUtil.set(REDIS_ADMIN_TOKEN + ":" + adminId, adminToken, REDIS_ADMIN_TOKEN_TIMEOUT);
        // 设置cookie
        setCookie(COOKIE_NAME_ADMIN_ID, adminId, COOKIE_ADMIN_MAX_AGE, domainName);
        setCookie(COOKIE_NAME_ADMIN_TOKEN, adminToken, COOKIE_ADMIN_MAX_AGE, domainName);
        setCookie(COOKIE_NAME_ADMIN_NAME, adminName, COOKIE_ADMIN_MAX_AGE, domainName);
    }

    /**
     * 查询分页配置
     *
     * @param list 查询数据（每页）
     * @param page 当前页
     * @return 封装分页查询结果
     */
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
