package cn.codeprobe.user.service;

import java.util.List;
import java.util.Map;

import cn.codeprobe.pojo.vo.UserBasicInfoVO;

/**
 * 门户：用户相关服务
 *
 * @author Lionido
 */
public interface UserPortalService {

    /**
     * 门户：获取用户基本信息 (缓存,数据库) 由于该接口调用量巨大，为了缓解数据库压力，设计了缓存
     * 
     * @param userId 用户ID
     * @return AppUser
     */
    UserBasicInfoVO getBasicUserInfo(String userId);

    /**
     * 通过Set获取用户map
     * 
     * @param idList 用户ID set
     * @return Map<String, UserBasicInfoVO>
     */
    Map<String, UserBasicInfoVO> getUserInfoMapByIdSet(List<String> idList);

}
