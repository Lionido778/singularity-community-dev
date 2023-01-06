package cn.codeprobe.user.service.impl;

import cn.codeprobe.enums.UserSex;
import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.user.mapper.AppUserMapper;
import org.n3r.idworker.Sid;

import javax.annotation.Resource;

public class BaseService {

    @Resource
    public AppUserMapper appUserMapper;

    @Resource
    public Sid sid;


    public final String NICKNAME_PREFIX = "用户_";
    public final Integer USER_ACTIVE_STATUS = UserStatus.INACTIVE.type;
    public final String USER_BIRTHDAY = "1900-1-1";
    public final Integer USER_SEX = UserSex.secret.type;
    public final Integer USER_TOTAL_INCOME = 0;
    public final String USER_FACE = "https://img2.baidu.com/it/u=132501275,3612619305&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";
    public final Integer USER_LOCKED = UserStatus.FROZEN.type;
}
