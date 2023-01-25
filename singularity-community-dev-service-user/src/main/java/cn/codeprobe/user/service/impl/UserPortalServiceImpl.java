package cn.codeprobe.user.service.impl;

import org.springframework.stereotype.Service;

import cn.codeprobe.pojo.po.AppUserDO;
import cn.codeprobe.user.service.UserPortalService;
import cn.codeprobe.user.service.base.UserBaseService;

/**
 * @author Lionido
 */
@Service
public class UserPortalServiceImpl extends UserBaseService implements UserPortalService {

    @Override
    public AppUserDO getUserInfo(String userId) {
        return getAppUserDO(userId);
    }

}
