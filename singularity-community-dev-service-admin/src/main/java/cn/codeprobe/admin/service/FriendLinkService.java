package cn.codeprobe.admin.service;

import cn.codeprobe.pojo.bo.FriendLinkBO;
import cn.codeprobe.pojo.mo.FriendLinkDO;

import java.util.List;

/**
 * @author Lionido
 */
public interface FriendLinkService {

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
