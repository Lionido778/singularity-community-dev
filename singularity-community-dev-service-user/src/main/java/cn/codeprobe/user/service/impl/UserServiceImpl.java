package cn.codeprobe.user.service.impl;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalException;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.user.service.UserService;
import cn.codeprobe.utils.DateUtil;
import cn.codeprobe.utils.DesensitizationUtil;
import cn.codeprobe.utils.IPUtil;
import cn.codeprobe.utils.JsonUtils;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl extends BaseService implements UserService {


    @Override
    public void getSMSCode(String mobile) {
        // 获取用户ip，限制用户只允许在60秒内发送一次短信
        String requestIp = IPUtil.getRequestIp(request);
        redisUtil.setnx60s(MOBILE_SMS_CODE + ":" + requestIp, requestIp);
        // 生成六位数随机数字验证码，并发送
        String randomCode = RandomUtil.randomNumbers(MOBILE_SMS_CODE_DIGITS);
//        smsUtil.sendSms(mobile, randomCode);
        // 将验证码存入redis，有效期30分钟，方便后续验证
        redisUtil.set(MOBILE_SMS_CODE + ":" + mobile, randomCode, MOBILE_SMS_CODE_TIMEOUT);
    }

    @Override
    public AppUser queryAppUserIsExist(String mobile) {
        // 构建example
        Example example = new Example(AppUser.class);
        Example.Criteria criteria = example.createCriteria();
        // 查询条件
        criteria.andEqualTo("mobile", mobile);
        // 查询用户
        AppUser appUser = appUserMapper.selectOneByExample(example);
        return appUser;
    }

    @Transactional
    @Override
    public AppUser createAppUser(String mobile) {
        AppUser appUser = new AppUser();
        // 分布式架构的互联网项目，随着用户流量激增，考虑到可扩展性，需要分库分表
        // 这个时候主键ID应该是（全库）唯一，而不能是主键自增
        appUser.setId(sid.nextShort());
        // 对昵称进行脱敏处理
        appUser.setNickname(NICKNAME_PREFIX + DesensitizationUtil.commonDisplay(mobile));
        // 手机号
        appUser.setMobile(mobile);
        // 头像
        appUser.setFace(USER_FACE);
        // 激活状态
        appUser.setActiveStatus(USER_UN_ACTIVE);
        // 生日（默认）
        appUser.setBirthday(DateUtil.stringToDate(USER_BIRTHDAY));
        //  性别（默认）
        appUser.setSex(USER_SEX);
        // 总收入 初始化 0
        appUser.setTotalIncome(USER_TOTAL_INCOME);
        appUser.setCreatedTime(new Date());
        appUser.setUpdatedTime(new Date());

        // 创建用户，并往数据库表里插入记录
        appUserMapper.insert(appUser);
        return appUser;
    }

    @Override
    public AppUser RegisterLogin(RegisterLoginBO registerLoginBO) {
        AppUser appUser = queryAppUserIsExist(registerLoginBO.getMobile());
        // 判断用户是否被冻结
        if (appUser != null && appUser.getActiveStatus() == USER_FROZEN) {
            GlobalException.Internal(ResponseStatusEnum.USER_FROZEN);
        } else if (appUser == null) {
            // 注册新用户
            appUser = createAppUser(registerLoginBO.getMobile());
        }
        UUID uToken = UUID.randomUUID();
        String uId = appUser.getId();
        // 保存分布式会话信息Cookie到redis，并响应保存到前端
        redisUtil.set(REDIS_USER_TOKEN + ":" + uId, uToken.toString().trim());
        setCookie(COOKIE_NAME_ID, uId, COOKIE_MAX_AGE);
        setCookie(COOKIE_NAME_TOKEN, uToken.toString(), COOKIE_MAX_AGE);
        // 短信验证码有效次数一次，用户成功注册或登陆时，短信验证码作废
        redisUtil.del(MOBILE_SMS_CODE + ":" + registerLoginBO.getMobile());
        return appUser;
    }

    @Override
    public AppUser getUserInfo(String userId) {
        // 先从缓存redis中查找user
        String jsonUser = redisUtil.get(REDIS_USER_INFO + ":" + userId);
        if (StrUtil.isNotBlank(jsonUser)) {
            // 如果有，返回
            return JsonUtils.jsonToPojo(jsonUser, AppUser.class);
        }
        // 如果没有，从数据库中查找user
        AppUser user = getUser(userId);
        // 并写入缓存redis
        redisUtil.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));
        return user;
    }

    @Override
    public void updateUserAccountInfo(UpdateUserInfoBO updateUserInfoBO) {
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(updateUserInfoBO, appUser);
        // 记录更新时间
        appUser.setUpdatedTime(new Date());
        // 完善用户信息后，激活用户
        appUser.setActiveStatus(USER_ACTIVE);
        // 有选择的更新数据库记录，为空的属性不会影响数据库记录
        int result = appUserMapper.updateByPrimaryKeySelective(appUser);
        if (result != 1) {
            GlobalException.Internal(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        // 更新后将最新的 user 写入缓存
        AppUser updatedUser = getUser(updateUserInfoBO.getId());
        redisUtil.set(REDIS_USER_INFO + ":" + updateUserInfoBO.getId(), JsonUtils.objectToJson(updatedUser));
    }


}
