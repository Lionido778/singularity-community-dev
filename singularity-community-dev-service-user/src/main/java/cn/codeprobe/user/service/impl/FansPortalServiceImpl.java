package cn.codeprobe.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.Fans;
import cn.codeprobe.pojo.po.User;
import cn.codeprobe.user.service.FansPortalService;
import cn.codeprobe.user.service.UserWriterService;
import cn.codeprobe.user.service.base.UserBaseService;

/**
 * 门户：粉丝相关服务
 *
 * @author Lionido
 */
@Service
public class FansPortalServiceImpl extends UserBaseService implements FansPortalService {

    @Resource
    private UserWriterService userWriterService;

    @Override
    public Boolean queryIsFollowed(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setFanId(fanId);
        fans.setWriterId(writerId);
        int count = fansMapper.selectCount(fans);
        return count > 0;
    }

    @Override
    public void followWriter(String writerId, String fanId) {
        User fanUserInfo = getAppUserDO(fanId);
        String id = idWorker.nextIdStr();
        Fans fans = new Fans();
        fans.setId(id);
        fans.setFanId(fanId);
        fans.setWriterId(writerId);
        // 设置冗余信息
        if (fanUserInfo != null) {
            fans.setFace(fanUserInfo.getFace());
            fans.setFanNickname(fanUserInfo.getNickname());
            fans.setProvince(fanUserInfo.getProvince());
            fans.setSex(fanUserInfo.getSex());
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.FANS_FOLLOW_FAILED);
        }
        // 数据库插入记录
        int res = fansMapper.insert(fans);
        if (res != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FANS_FOLLOW_FAILED);
        }

        // 缓存实现 关注数、粉丝数累加、统计
        // 作者粉丝数加一
        redisUtil.increment(REDIS_WRITER_FANS_COUNT + ":" + writerId, 1);
        // 用户关注数加一
        redisUtil.increment(REDIS_WRITER_FOLLOWED_COUNT + ":" + fanId, 1);
    }

    @Override
    public void unFollowWriter(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setFanId(fanId);
        int res = fansMapper.delete(fans);
        if (res != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FANS_UN_FOLLOW_FAILED);
        }
        // 缓存实现 关注数、粉丝数累加、统计
        // 作者粉丝数减一
        redisUtil.decrement(REDIS_WRITER_FANS_COUNT + ":" + writerId, 1);
        // 用户关注数减一
        redisUtil.decrement(REDIS_WRITER_FOLLOWED_COUNT + ":" + fanId, 1);
    }

}
