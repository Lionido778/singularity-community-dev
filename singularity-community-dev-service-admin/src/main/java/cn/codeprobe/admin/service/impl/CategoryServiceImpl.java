package cn.codeprobe.admin.service.impl;

import cn.codeprobe.admin.service.CategoryService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.Category;
import cn.codeprobe.pojo.bo.CategoryBO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lionido
 */
@Service
public class CategoryServiceImpl extends AdminBaseService implements CategoryService {
    @Override
    public void saveOrUpdateCategory(CategoryBO categoryBO) {
        // 判断 更新或者是新添加
        Integer id = categoryBO.getId();
        Category category = new Category();
        BeanUtils.copyProperties(categoryBO, category);
        int result;
        if (id == null) {
            // 添加
            result = categoryMapper.insert(category);
        } else {
            // 更新
            result = categoryMapper.updateByPrimaryKeySelective(category);
        }
        if (result != 1) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_CATEGORY_ADD_FAILED);
        }
    }

    @Override
    public List<Category> getCategories() {
        return categoryMapper.selectAll();
    }

    @Override
    public void delete(Integer categoryId) {
        int result = categoryMapper.deleteByPrimaryKey(categoryId);
        if (result != 1) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_CATEGORY_DELETE_FAILED);
        }
    }
}
