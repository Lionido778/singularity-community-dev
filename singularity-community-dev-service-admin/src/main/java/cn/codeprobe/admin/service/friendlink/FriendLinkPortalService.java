package cn.codeprobe.admin.service.friendlink;

import java.util.List;

import cn.codeprobe.pojo.mo.FriendLink;

/**
 * 门户: 友情链接相关接口
 *
 * @author Lionido
 */
public interface FriendLinkPortalService {

    /**
     * 门户： 获取所有友情链接
     * 
     * @param isDelete 未删除
     * @return List<FriendLinkDO>
     */
    List<FriendLink> listFriendLinks(Integer isDelete);

}
