package cn.codeprobe.user.service.base;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.enums.UserSex;
import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.pojo.po.AppUserDO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.user.mapper.AppUserMapper;
import cn.codeprobe.utils.IdWorker;
import cn.codeprobe.utils.RedisUtil;
import cn.codeprobe.utils.SmsUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 基础service ，提供共用常量、变量、方法等
 *
 * @author Lionido
 */
public class UserBaseService extends ApiController {

    @Resource
    public AppUserMapper appUserMapper;
    @Resource
    public IdWorker idWorker;
    @Resource
    public RedisUtil redisUtil;
    @Resource
    public SmsUtil smsUtil;

    /**
     * domain-name
     */
    @Value("${website.domain-name}")
    public String domainName;

    /**
     * sms
     */
    public static final String MOBILE_SMS_CODE = "mobile_sms_code";
    public static final Long MOBILE_SMS_CODE_TIMEOUT = (long) (30 * 60);
    public static final Integer MOBILE_SMS_CODE_DIGITS = 6;

    /**
     * user
     */
    public static final String NICKNAME_PREFIX = "用户_";
    public static final Integer USER_UN_ACTIVE = UserStatus.INACTIVE.type;
    public static final Integer USER_ACTIVE = UserStatus.ACTIVE.type;
    public static final Integer USER_FROZEN = UserStatus.FROZEN.type;
    public static final String USER_BIRTHDAY = "1900-1-1";
    public static final Integer USER_SEX = UserSex.SECRET.type;
    public static final Integer USER_TOTAL_INCOME = 0;
    public static final String USER_FACE = "https://img2.baidu.com/it/u=132501275,3612619305&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";

    /**
     * token & cookie
     */
    public static final String REDIS_USER_TOKEN = "user_token";
    public static final String REDIS_USER_INFO = "user_info";
    public static final Integer REDIS_USER_TOKEN_TIMEOUT = 60 * 60;
    public static final String COOKIE_NAME_ID = "uid";
    public static final String COOKIE_NAME_TOKEN = "utoken";
    public static final Integer COOKIE_MAX_AGE = 7 * 24 * 60 * 60;
    public static final Integer COOKIE_DELETE = 0;


    /**
     * 获取用户
     */
    public AppUserDO getUser(String userId) {
        return appUserMapper.selectByPrimaryKey(userId);
    }

    public AppUserDO queryAppUserByMobile(String mobile) {
        // 构建example
        Example example = new Example(AppUserDO.class);
        Example.Criteria criteria = example.createCriteria();
        // 查询条件
        criteria.andEqualTo("mobile", mobile);
        // 查询用户
        return appUserMapper.selectOneByExample(example);
    }

    /**
     * 查询分页配置
     *
     * @param list 查询数据（每页）
     * @param page 当前页
     * @return 封装分页查询结果
     */
    public PagedGridResult setPageGrid(List<?> list, int page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setTotal(pageInfo.getPages());
        gridResult.setRecords(pageInfo.getTotal());
        return gridResult;
    }

}
