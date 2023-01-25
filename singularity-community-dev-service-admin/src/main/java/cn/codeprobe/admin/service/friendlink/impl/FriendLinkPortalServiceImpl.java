package cn.codeprobe.admin.service.friendlink.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.admin.service.friendlink.FriendLinkPortalService;
import cn.codeprobe.pojo.mo.FriendLinkDO;

/**
 * @author Lionido
 */
@Service
public class FriendLinkPortalServiceImpl extends AdminBaseService implements FriendLinkPortalService {

    @Override
    public List<FriendLinkDO> listFriendLinks(Integer isDelete) {
        return friendLinkRepository.getAllByIsDelete(isDelete);
    }

}
