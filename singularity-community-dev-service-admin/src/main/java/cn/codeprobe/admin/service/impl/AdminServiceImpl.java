package cn.codeprobe.admin.service.impl;

import cn.codeprobe.admin.service.AdminService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.AdminUser;
import cn.codeprobe.pojo.bo.NewAdminBO;
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
        AdminUser adminUser = queryAdminByUsername(username);
        return adminUser != null;
    }

    @Override
    public void createAdminUser(NewAdminBO newAdminBO) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(sid.nextShort());
        adminUser.setUsername(newAdminBO.getUsername());
        adminUser.setAdminName(newAdminBO.getAdminName());
        // 密码加密
        String encryptPassword = encryptPassword(newAdminBO.getPassword());
        adminUser.setPassword(encryptPassword);
        String faceId = newAdminBO.getFaceId();
        // 如果上传了人脸识别则会有 faceId
        if (CharSequenceUtil.isNotBlank(faceId)) {
            adminUser.setFaceId(faceId);
        }
        adminUser.setCreatedTime(new Date());
        adminUser.setUpdatedTime(new Date());

        int result = adminUserMapper.insertSelective(adminUser);
        if (result != 1) {
            // 管理员添加失败
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }

    @Override
    public PagedGridResult queryAdminUserList(int page, int pageSize) {
        Example example = new Example(AdminUser.class);
        example.orderBy("createdTime").desc();

        PageMethod.startPage(page, pageSize);
        List<AdminUser> adminUserList = adminUserMapper.selectByExample(example);
        if (adminUserList.isEmpty()){
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_QUERY_ERROR);
        }
        return setterPageGrid(adminUserList, page);
    }
}
