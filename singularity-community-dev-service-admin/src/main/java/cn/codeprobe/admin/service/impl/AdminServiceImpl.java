package cn.codeprobe.admin.service.impl;

import cn.codeprobe.admin.service.AdminService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.pojo.po.AdminUserDO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;
import com.github.pagehelper.page.PageMethod;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author Lionido
 */
@Service
public class AdminServiceImpl extends AdminBaseService implements AdminService {
    @Override
    public Boolean checkAdminIsExist(String username) {
        AdminUserDO adminUserDO = queryAdminByUsername(username);
        return adminUserDO != null;
    }

    @Override
    public void saveAdminUser(NewAdminBO newAdminBO) {
        AdminUserDO adminUserDO = new AdminUserDO();
        adminUserDO.setId(idWorker.nextIdStr());
        adminUserDO.setUsername(newAdminBO.getUsername());
        adminUserDO.setAdminName(newAdminBO.getAdminName());
        // 密码加密
        String encryptPassword = encryptPassword(newAdminBO.getPassword());
        adminUserDO.setPassword(encryptPassword);
        String faceId = newAdminBO.getFaceId();
        // 如果上传了人脸识别则会有 faceId
        if (CharSequenceUtil.isNotBlank(faceId)) {
            adminUserDO.setFaceId(faceId);
        }
        adminUserDO.setCreatedTime(new Date());
        adminUserDO.setUpdatedTime(new Date());

        int result = adminUserMapper.insertSelective(adminUserDO);
        if (result != 1) {
            // 管理员添加失败
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }

    @Override
    public PagedGridResult pageListAdminUsers(int page, int pageSize) {
        Example example = new Example(AdminUserDO.class);
        example.orderBy("createdTime").desc();

        PageMethod.startPage(page, pageSize);
        List<AdminUserDO> adminUserDOList = adminUserMapper.selectByExample(example);
        if (adminUserDOList.isEmpty()) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_QUERY_ERROR);
        }
        return setterPageGrid(adminUserDOList, page);
    }
}
