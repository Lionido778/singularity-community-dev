package cn.codeprobe.user.service;

/**
 * 门户：粉丝相关服务
 *
 * @author Lionido
 */
public interface FansPortalService {

    /**
     * 门户：是否关注该作者
     *
     * @param writerId 作者ID
     * @param fanId 作者ID
     * @return Boolean
     */
    Boolean queryIsFollowed(String writerId, String fanId);

    /**
     * 关注 作家
     * 
     * @param writerId 作家ID
     * @param fanId 粉丝ID
     */
    void followWriter(String writerId, String fanId);

    /**
     * 取关 作家
     *
     * @param writerId 作家ID
     * @param fanId 粉丝ID
     */
    void unFollowWriter(String writerId, String fanId);
}
