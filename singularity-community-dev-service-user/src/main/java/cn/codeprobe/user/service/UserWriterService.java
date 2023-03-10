package cn.codeprobe.user.service;

import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.pojo.po.User;
import cn.codeprobe.pojo.vo.UserAccountInfoVO;

/**
 * 创作中心：用户相关服务
 *
 * @author Lionido
 */
public interface UserWriterService {

    /**
     * 创作中心：注册用户
     *
     * @param mobile 手机号
     * @return AppUserDO
     */
    User saveAppUser(String mobile);

    /**
     * 创作中心：获取用户 (缓存,数据库) 由于该接口调用量巨大，为了缓解数据库压力，设计了缓存
     *
     * @param userId 用户ID
     * @return UserAccountInfoVO
     */
    UserAccountInfoVO getUserAccountInfo(String userId);

    /**
     * 创作中心：更新用户账户信息
     *
     * @param updateUserInfoBO 用户账户表单信息
     */
    void updateUserAccountInfo(UpdateUserInfoBO updateUserInfoBO);

}
