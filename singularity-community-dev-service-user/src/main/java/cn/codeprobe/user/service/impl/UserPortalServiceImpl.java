package cn.codeprobe.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        User user = getUser(userId);
        UserBasicInfoVO userBasicInfoVO = new UserBasicInfoVO();
        // po -> vo
        BeanUtils.copyProperties(user, userBasicInfoVO);
        // 拼接 fans 关注数和粉丝数
        String fansCounts = redisUtil.get(REDIS_WRITER_FANS_COUNT + ":" + userId);
        String followCounts = redisUtil.get(REDIS_WRITER_FOLLOWED_COUNT + ":" + userId);
        if (CharSequenceUtil.isBlank(fansCounts)) {
            userBasicInfoVO.setFansCounts(String.valueOf(FANS_DEFAULT_COUNT));
        }
        if (CharSequenceUtil.isBlank(followCounts)) {
            userBasicInfoVO.setFollowCounts(String.valueOf(FOLLOWED_DEFAULT_COUNT));
        }
        userBasicInfoVO.setFansCounts(fansCounts);
        userBasicInfoVO.setFollowCounts(String.valueOf(followCounts));
        return userBasicInfoVO;
    }

    @Override
    public Map<String, UserBasicInfoVO> getUserInfoMapByIdSet(List<String> idList) {
        Map<String, UserBasicInfoVO> map = new HashMap<>(20);
        for (String id : idList) {
            UserBasicInfoVO userBasicInfoVO = getBasicUserInfo(id);
            if (userBasicInfoVO == null) {
                continue;
            }
            map.put(id, userBasicInfoVO);
        }
        return map;
    }
}
