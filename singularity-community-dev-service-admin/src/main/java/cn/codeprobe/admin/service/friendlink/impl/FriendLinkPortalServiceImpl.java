package cn.codeprobe.admin.service.friendlink.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.admin.service.friendlink.FriendLinkPortalService;
import cn.codeprobe.pojo.mo.FriendLink;

/**
 * @author Lionido
 */
@Service
public class FriendLinkPortalServiceImpl extends AdminBaseService implements FriendLinkPortalService {

    @Override
    public List<FriendLink> listFriendLinks(Integer isDelete) {
        return friendLinkRepository.getAllByIsDelete(isDelete);
    }

}
