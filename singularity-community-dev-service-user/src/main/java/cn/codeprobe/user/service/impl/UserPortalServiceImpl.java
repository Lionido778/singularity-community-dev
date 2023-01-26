package cn.codeprobe.user.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.codeprobe.pojo.po.AppUserDO;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.user.service.UserPortalService;
import cn.codeprobe.user.service.base.UserBaseService;

/**
 * @author Lionido
 */
@Service
public class UserPortalServiceImpl extends UserBaseService implements UserPortalService {

    @Override
    public UserBasicInfoVO getBasicUserInfo(String userId) {
        // 执行查询操作
        AppUserDO appUserDO = getAppUserDO(userId);
        UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
        // pojo -> vo
        BeanUtils.copyProperties(appUserDO, userBasicInfoVO);
        // 拼接 fans 关注数和粉丝数
        String fansCounts = redisUtil.get(REDIS_WRITER_FANS_COUNT + ":" + userId);
        String followCounts = redisUtil.get(REDIS_WRITER_FOLLOWED_COUNT + ":" + userId);
        userBasicInfoVO.setFansCounts(fansCounts);
        userBasicInfoVO.setFollowCounts(followCounts);
        return userBasicInfoVO;
    }

    @Override
    public AppUserDO getUserInfo(String userId) {
        return getAppUserDO(userId);
    }

}
