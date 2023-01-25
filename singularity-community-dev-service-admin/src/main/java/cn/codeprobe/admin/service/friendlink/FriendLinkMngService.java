package cn.codeprobe.admin.service.friendlink;

import java.util.List;

import cn.codeprobe.pojo.bo.FriendLinkBO;
import cn.codeprobe.pojo.mo.FriendLinkDO;

/**
 * 管理中心: 友情链接管理service
 *
 * @author Lionido
 */
public interface FriendLinkMngService {

    /**
     * 保存或更新 FriendLink
     *
     * @param friendLinkBO 友情链接
     */
    void saveOrUpdateFriendLink(FriendLinkBO friendLinkBO);

    /**
     * 获取FriendLinkList
     *
     * @return list
     */
    List<FriendLinkDO> listFriendLinks();

    /**
     * 删除 友情链接
     *
     * @param linkId id
     */
    void removeFriendLink(String linkId);

    /**
     * 检查友情链接是否存在
     *
     * @param linkName 链接名称
     * @return true / false
     */
    Boolean checkFriendLinkIsExist(String linkName);
}
