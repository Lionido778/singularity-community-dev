package cn.codeprobe.user.service.base;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import com.github.pagehelper.PageInfo;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.enums.UserSex;
import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.pojo.po.User;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.user.mapper.FansMapper;
import cn.codeprobe.user.mapper.UserMapper;
import cn.codeprobe.utils.IdWorker;
import cn.codeprobe.utils.RedisUtil;
import cn.codeprobe.utils.SmsUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * 基础service ，提供共用常量、变量、方法等
 *
 * @author Lionido
 */
public class UserBaseService extends ApiController {

    /**
     * sms
     */
    public static final String MOBILE_SMS_CODE = "mobile_sms_code";
    public static final Long MOBILE_SMS_CODE_TIMEOUT = (long)(30 * 60);
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
    public static final String USER_FACE =
        "https://img2.baidu.com/it/u=132501275,3612619305&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";
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
     * fans
     */
    public static final String REDIS_WRITER_FOLLOWED_COUNT = "writer_followed_count";
    public static final String REDIS_WRITER_FANS_COUNT = "writer_fans_count";
    public static final String FANS_DEFAULT_COUNT = "0";
    public static final String FOLLOWED_DEFAULT_COUNT = "0";
    public static final String[] REGIONS =
        {"北京", "天津", "上海", "重庆", "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南",
            "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾", "内蒙古", "广西", "西藏", "宁夏", "新疆", "香港", "澳门"};

    @Resource
    public UserMapper userMapper;
    @Resource
    public FansMapper fansMapper;
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
     * 通过手机查询用户
     * 
     * @param mobile 手机号
     * @return user
     */
    public User queryUserByMobile(String mobile) {
        // 构建example
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        // 查询条件
        criteria.andEqualTo("mobile", mobile);
        // 查询用户
        return userMapper.selectOneByExample(example);
    }

    /**
     * 配置分页
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

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return user
     */
    public User getUser(String userId) {
        // 先从缓存redis中查找user
        String jsonUser = redisUtil.get(REDIS_USER_INFO + ":" + userId);
        if (CharSequenceUtil.isNotBlank(jsonUser)) {
            // 如果有，返回
            return JSONUtil.toBean(jsonUser, User.class);
        }
        // 如果没有，从数据库中查找user
        User user = userMapper.selectByPrimaryKey(userId);
        // 并写入缓存redis
        redisUtil.set(REDIS_USER_INFO + ":" + userId, JSONUtil.toJsonStr(user));
        return user;
    }

}
