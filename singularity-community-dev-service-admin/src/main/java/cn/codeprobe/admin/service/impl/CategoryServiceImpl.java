package cn.codeprobe.admin.service.impl;

import cn.codeprobe.admin.service.CategoryService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.CategoryBO;
import cn.codeprobe.pojo.po.CategoryDO;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Lionido
 */
@Service
public class CategoryServiceImpl extends AdminBaseService implements CategoryService {
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
        CategoryDO categoryDO = new CategoryDO();
        BeanUtils.copyProperties(categoryBO, categoryDO);
        int result;
        if (id == null) {
            // 添加
            result = categoryMapper.insert(categoryDO);
        } else {
            // 更新
            result = categoryMapper.updateByPrimaryKeySelective(categoryDO);
        }
        if (result != 1) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_CATEGORY_ADD_FAILED);
        }
        // 删除缓存，避免前端读取旧数据，保证数据同步
        redisUtil.del(REDIS_ALL_CATEGORIES + ":" + REDIS_ALL_CATEGORIES);
    }


    @Override
    public List<CategoryDO> getCategories() {
        String jsonCategoriesList = redisUtil.get(REDIS_ALL_CATEGORIES + ":" + REDIS_ALL_CATEGORIES);
        if (CharSequenceUtil.isNotBlank(jsonCategoriesList)) {
            return JSONUtil.toList(jsonCategoriesList, CategoryDO.class);
        } else {
            List<CategoryDO> list = categoryMapper.selectAll();
            redisUtil.set(REDIS_ALL_CATEGORIES + ":" + REDIS_ALL_CATEGORIES, JSONUtil.toJsonStr(list));
            return list;
        }
    }

    @Override
    public List<CategoryDO> listCategories() {
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
        Example example = new Example(CategoryDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", categoryName);
        CategoryDO categoryDO = categoryMapper.selectOneByExample(example);
        return categoryDO != null;
    }

}
