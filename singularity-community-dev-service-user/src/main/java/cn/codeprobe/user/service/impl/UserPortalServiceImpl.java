package cn.codeprobe.user.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.codeprobe.pojo.po.User;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.user.service.UserPortalService;
import cn.codeprobe.user.service.base.UserBaseService;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@Service
public class UserPortalServiceImpl extends UserBaseService implements UserPortalService {

    @Override
    public UserBasicInfoVO getBasicUserInfo(String userId) {
        // 执行查询操作
        User user = getAppUserDO(userId);
        UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
        // pojo -> vo
        BeanUtils.copyProperties(user, userBasicInfoVO);
        // 拼接 fans 关注数和粉丝数
        String fansCounts = redisUtil.get(REDIS_WRITER_FANS_COUNT + ":" + userId);
        String followCounts = redisUtil.get(REDIS_WRITER_FOLLOWED_COUNT + ":" + userId);
        if (CharSequenceUtil.isNotBlank(fansCounts)) {
            userBasicInfoVO.setFansCounts(fansCounts);
        } else {
            userBasicInfoVO.setFansCounts(String.valueOf(FANS_DEFAULT_COUNT));
        }
        if (CharSequenceUtil.isNotBlank(followCounts)) {
            userBasicInfoVO.setFollowCounts(String.valueOf(followCounts));
        } else {
            userBasicInfoVO.setFollowCounts(String.valueOf(FOLLOWED_DEFAULT_COUNT));
        }
        return userBasicInfoVO;
    }

}
