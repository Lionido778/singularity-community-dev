package cn.codeprobe.user.service.impl;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.user.service.UserService;
import cn.codeprobe.user.service.base.UserBaseService;
import cn.codeprobe.utils.DateUtil;
import cn.codeprobe.utils.DesensitizationUtil;
import cn.codeprobe.utils.JsonUtil;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author Lionido
 */
@Service
public class UserServiceImpl extends UserBaseService implements UserService {

    @Override
    public AppUser queryAppUserIsExist(String mobile) {
        // 构建example
        Example example = new Example(AppUser.class);
        Example.Criteria criteria = example.createCriteria();
        // 查询条件
        criteria.andEqualTo("mobile", mobile);
        // 查询用户
        return appUserMapper.selectOneByExample(example);
    }

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
    public AppUser getUserInfo(String userId) {
        // 先从缓存redis中查找user
        String jsonUser = redisUtil.get(REDIS_USER_INFO + ":" + userId);
        if (CharSequenceUtil.isNotBlank(jsonUser)) {
            // 如果有，返回
            return JsonUtil.jsonToPojo(jsonUser, AppUser.class);
        }
        // 如果没有，从数据库中查找user
        AppUser user = getUser(userId);
        // 并写入缓存redis
        redisUtil.set(REDIS_USER_INFO + ":" + userId, JsonUtil.objectToJson(user));
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
        // 缓存数据双写（最新数据写入数据库和redis）一致。更新前删除缓存中原有的userInfo
        redisUtil.del(REDIS_USER_INFO + ":" + updateUserInfoBO.getId());
        // 有选择的更新数据库记录，为空的属性不会影响数据库记录
        int result = appUserMapper.updateByPrimaryKeySelective(appUser);
        if (result != 1) {
            GlobalExceptionManage.internal(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        // 更新后将最新的 user 写入缓存
        AppUser updatedUser = getUser(updateUserInfoBO.getId());
        redisUtil.set(REDIS_USER_INFO + ":" + updateUserInfoBO.getId(), JsonUtil.objectToJson(updatedUser));
        try {
            Thread.sleep(100);
            // 缓存双删策略
            redisUtil.del(REDIS_USER_INFO + ":" + updateUserInfoBO.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
