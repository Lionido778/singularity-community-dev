package cn.codeprobe.admin.service.category.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.admin.service.category.CategoryMngService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.CategoryBO;
import cn.codeprobe.pojo.po.Category;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
@Service
public class CategoryMngServiceImpl extends AdminBaseService implements CategoryMngService {

    @Override
    public void saveOrUpdateCategory(CategoryBO categoryBO) {
        // 判断 分类名是否已存在
        String name = categoryBO.getName();
        Boolean isExist = checkCategoryIsExist(name);
        if (Boolean.TRUE.equals(isExist)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_CATEGORY_IS_EXISTED);
        }
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
        // 删除缓存，避免前端读取旧数据，保证数据同步
        redisUtil.del(REDIS_ALL_CATEGORIES + ":" + REDIS_ALL_CATEGORIES);
    }

    @Override
    public List<Category> listCategories() {
        return categoryMapper.selectAll();
    }

    @Override
    public void removeCategory(Integer categoryId) {
        int result = categoryMapper.deleteByPrimaryKey(categoryId);
        if (result != 1) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_CATEGORY_DELETE_FAILED);
        }
        // 删除缓存，避免前端读取旧数据，保证数据同步
        redisUtil.del(REDIS_ALL_CATEGORIES + ":" + REDIS_ALL_CATEGORIES);
    }

    @Override
    public Boolean checkCategoryIsExist(String categoryName) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", categoryName);
        Category category = categoryMapper.selectOneByExample(example);
        return category != null;
    }

}
