package cn.codeprobe.admin.service.category.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.admin.service.category.CategoryPortalService;
import cn.codeprobe.pojo.po.Category;

/**
 * @author Lionido
 */
@Service
public class CategoryPortalServiceImpl extends AdminBaseService implements CategoryPortalService {

    @Override
    public List<Category> getCategories() {
        return getCategoryDOList();
    }

}
