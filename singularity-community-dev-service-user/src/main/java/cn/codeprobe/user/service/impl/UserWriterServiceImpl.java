package cn.codeprobe.user.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.pojo.po.User;
import cn.codeprobe.pojo.vo.UserAccountInfoVO;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.user.service.UserWriterService;
import cn.codeprobe.user.service.base.UserBaseService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author Lionido
 */
@Service
public class UserWriterServiceImpl extends UserBaseService implements UserWriterService {

    @Value("${server.port}")
    private String port;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User saveAppUser(String mobile) {
        User user = new User();
        // 分布式架构的互联网项目，随着用户流量激增，考虑到可扩展性，需要分库分表
        // 这个时候主键ID应该是（全库）唯一，而不能是主键自增
        user.setId(idWorker.nextIdStr());
        // 对昵称进行脱敏处理
        user.setNickname(NICKNAME_PREFIX + DesensitizedUtil.mobilePhone(mobile));
        // 手机号
        user.setMobile(mobile);
        // 头像
        user.setFace(USER_FACE);
        // 激活状态
        user.setActiveStatus(USER_UN_ACTIVE);
        // 生日（默认）
        user.setBirthday(DateUtil.parse(USER_BIRTHDAY));
        // 性别（默认）
        user.setSex(USER_SEX);
        // 总收入 初始化 0
        user.setTotalIncome(USER_TOTAL_INCOME);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        // 创建用户，并往数据库表里插入记录
        userMapper.insert(user);
        return user;
    }

    @Override
    public void updateUserAccountInfo(UpdateUserInfoBO updateUserInfoBO) {
        User user = new User();
        BeanUtils.copyProperties(updateUserInfoBO, user);
        // 记录更新时间
        user.setUpdatedTime(new Date());
        // 完善用户信息后，激活用户
        user.setActiveStatus(USER_ACTIVE);
        // 缓存数据双写（最新数据写入数据库和redis）一致。更新前删除缓存中原有的userInfo
        redisUtil.del(REDIS_USER_INFO + ":" + updateUserInfoBO.getId());
        // 有选择的更新数据库记录，为空的属性不会影响数据库记录
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result != 1) {
            GlobalExceptionManage.internal(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        // 更新后将最新的 user 写入缓存
        User updatedUser = getUser(updateUserInfoBO.getId());
        redisUtil.set(REDIS_USER_INFO + ":" + updateUserInfoBO.getId(), JSONUtil.toJsonStr(updatedUser));
        try {
            Thread.sleep(100);
            // 缓存双删策略
            redisUtil.del(REDIS_USER_INFO + ":" + updateUserInfoBO.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public UserBasicInfoVO getUserBasicInfo(String userId) {
        System.out.println("port: " + port);
        User user = getUser(userId);
        UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
        // po -> vo
        BeanUtils.copyProperties(user, userBasicInfoVO);
        return userBasicInfoVO;
    }

    @Override
    public UserAccountInfoVO getUserAccountInfo(String userId) {
        User user = getUser(userId);
        // po -> vo
        UserAccountInfoVO userAccountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, userAccountInfoVO);
        return userAccountInfoVO;
    }
}
