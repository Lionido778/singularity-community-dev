package cn.codeprobe.user.service;

import java.util.Date;

import cn.codeprobe.result.page.PagedGridResult;

/**
 * 管理中心：用户相关服务
 *
 * @author Lionido
 */
public interface UserMngService {

    /**
     * 管理中心：获取用户分页列表
     *
     * @param nickname 登陆名
     * @param activeStatus 激活状态
     * @param startTime 创建起始时间
     * @param endTime 创建截止时间
     * @param page 当前页
     * @param pageSize 当前页查询数量
     * @return 用户分页列表
     */
    PagedGridResult pageListUsers(String nickname, Integer activeStatus, Date startTime, Date endTime, Integer page,
        Integer pageSize);

    /**
     * 管理中心：冻结或者解冻用户
     *
     * @param userId 用户ID
     * @param doStatus 2 冻结，1解冻
     */
    void freezeUserOrNot(String userId, Integer doStatus);
}
