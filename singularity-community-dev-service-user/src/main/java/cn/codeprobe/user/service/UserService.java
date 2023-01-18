package cn.codeprobe.user.service;


import cn.codeprobe.pojo.bo.UpdateUserInfoBO;
import cn.codeprobe.pojo.po.AppUserDO;

/**
 * 用户相关服务
 *
 * @author Lionido
 */
public interface UserService {


    /**
     * 根据手机号查询用户是否存在
     *
     * @param mobile 手机号
     * @return AppUser
     */
    AppUserDO queryAppUserIsExist(String mobile);

    /**
     * 注册用户
     *
     * @param mobile 手机号
     * @return AppUser
     */
    AppUserDO saveAppUser(String mobile);

    /**
     * 获取用户 (缓存,数据库)
     * 由于该接口调用量巨大，为了缓解数据库压力，设计了缓存
     *
     * @param userId 用户ID
     * @return AppUser
     */
    AppUserDO getUserInfo(String userId);

    /**
     * 更新用户账户信息
     *
     * @param updateUserInfoBO 用户账户表单信息
     */
    void updateUserAccountInfo(UpdateUserInfoBO updateUserInfoBO);

}
