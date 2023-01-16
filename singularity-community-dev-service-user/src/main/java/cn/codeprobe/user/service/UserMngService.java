package cn.codeprobe.user.service;

import cn.codeprobe.result.page.PagedGridResult;

import java.util.Date;

/**
 * @author Lionido
 */
public interface UserMngService {

    /**
     * 获取用户列表
     */
    PagedGridResult getUserList(String nickname, Integer activeStatus, Date startTime, Date endTime, Integer page, Integer pageSize);

    /**
     * 冻结或者解冻用户
     *
     * @param userId   用户ID
     * @param doStatus 2 冻结，1解冻
     */
    void freezeUserOrNot(String userId, Integer doStatus);
}
