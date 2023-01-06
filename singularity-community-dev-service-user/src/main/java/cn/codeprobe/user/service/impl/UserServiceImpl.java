package cn.codeprobe.user.service.impl;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalException;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.pojo.bo.RegisterLoginBO;
import cn.codeprobe.user.service.RegisterLogin;
import cn.codeprobe.utils.DateUtil;
import cn.codeprobe.utils.DesensitizationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl extends BaseService implements RegisterLogin {


    @Override
    public AppUser queryAppUserByMobile(String mobile) {
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
        appUser.setActiveStatus(USER_ACTIVE_STATUS);
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
        AppUser appUser = queryAppUserByMobile(registerLoginBO.getMobile());
        if (appUser != null && appUser.getActiveStatus() == USER_LOCKED) {
            GlobalException.Internal(ResponseStatusEnum.USER_FROZEN);
        } else if (appUser == null) {
            appUser = createAppUser(registerLoginBO.getMobile());
        }
        // TODO 登录
        System.out.println("用户已存在！");
        return appUser;
    }


}
