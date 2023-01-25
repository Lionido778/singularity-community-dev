package cn.codeprobe.user.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.pojo.po.AppUserDO;
import cn.codeprobe.user.service.UserWriterService;
import cn.codeprobe.user.service.base.UserBaseService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.json.JSONUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
@Service
public class UserWriterServiceImpl extends UserBaseService implements UserWriterService {

    @Override
    public AppUserDO queryAppUserIsExist(String mobile) {
        // 构建example
        Example example = new Example(AppUserDO.class);
        Example.Criteria criteria = example.createCriteria();
        // 查询条件
        criteria.andEqualTo("mobile", mobile);
        // 查询用户
        return appUserMapper.selectOneByExample(example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppUserDO saveAppUser(String mobile) {
        AppUserDO appUserDO = new AppUserDO();
        // 分布式架构的互联网项目，随着用户流量激增，考虑到可扩展性，需要分库分表
        // 这个时候主键ID应该是（全库）唯一，而不能是主键自增
        appUserDO.setId(idWorker.nextIdStr());
        // 对昵称进行脱敏处理
        appUserDO.setNickname(NICKNAME_PREFIX + DesensitizedUtil.mobilePhone(mobile));
        // 手机号
        appUserDO.setMobile(mobile);
        // 头像
        appUserDO.setFace(USER_FACE);
        // 激活状态
        appUserDO.setActiveStatus(USER_UN_ACTIVE);
        // 生日（默认）
        appUserDO.setBirthday(DateUtil.parse(USER_BIRTHDAY));
        // 性别（默认）
        appUserDO.setSex(USER_SEX);
        // 总收入 初始化 0
        appUserDO.setTotalIncome(USER_TOTAL_INCOME);
        appUserDO.setCreatedTime(new Date());
        appUserDO.setUpdatedTime(new Date());

        // 创建用户，并往数据库表里插入记录
        appUserMapper.insert(appUserDO);
        return appUserDO;
    }

    @Override
    public AppUserDO getUserInfo(String userId) {
        return getAppUserDO(userId);
    }

    @Override
    public void updateUserAccountInfo(UpdateUserInfoBO updateUserInfoBO) {
        AppUserDO appUserDO = new AppUserDO();
        BeanUtils.copyProperties(updateUserInfoBO, appUserDO);
        // 记录更新时间
        appUserDO.setUpdatedTime(new Date());
        // 完善用户信息后，激活用户
        appUserDO.setActiveStatus(USER_ACTIVE);
        // 缓存数据双写（最新数据写入数据库和redis）一致。更新前删除缓存中原有的userInfo
        redisUtil.del(REDIS_USER_INFO + ":" + updateUserInfoBO.getId());
        // 有选择的更新数据库记录，为空的属性不会影响数据库记录
        int result = appUserMapper.updateByPrimaryKeySelective(appUserDO);
        if (result != 1) {
            GlobalExceptionManage.internal(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        // 更新后将最新的 user 写入缓存
        AppUserDO updatedUser = getUser(updateUserInfoBO.getId());
        redisUtil.set(REDIS_USER_INFO + ":" + updateUserInfoBO.getId(), JSONUtil.toJsonStr(updatedUser));
        try {
            Thread.sleep(100);
            // 缓存双删策略
            redisUtil.del(REDIS_USER_INFO + ":" + updateUserInfoBO.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
