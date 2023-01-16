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

}
