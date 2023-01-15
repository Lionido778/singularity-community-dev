package cn.codeprobe.admin.service;

import cn.codeprobe.pojo.bo.FriendLinkBO;
import cn.codeprobe.pojo.mo.FriendLinkMO;

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
    List<FriendLinkMO> getFriendLinks();

    /**
     * 删除 友情链接
     *
     * @param linkId id
     */
    void delete(String linkId);

    Boolean checkFriendLinkIsExist(String linkName);
}
