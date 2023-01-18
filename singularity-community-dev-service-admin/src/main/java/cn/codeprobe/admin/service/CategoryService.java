package cn.codeprobe.admin.service;

import cn.codeprobe.pojo.bo.CategoryBO;
import cn.codeprobe.pojo.po.CategoryDO;

import java.util.List;

/**
 * @author Lionido
 */

public interface CategoryService {

    /**
     * 保存或更新 Category
     *
     * @param categoryBO 友情链接
     */
    void saveOrUpdateCategory(CategoryBO categoryBO);

    /**
     * 创作中心 获取 category List
     *
     * @return list
     */
    List<CategoryDO> getCategories();

    /**
     * 管理中心 获取 category List
     *
     * @return list
     */
    List<CategoryDO> listCategories();

    /**
     * 删除 分类
     *
     * @param categoryId id
     */
    void removeCategory(Integer categoryId);

    /**
     * 查询文章分类是否存在
     *
     * @param categoryName 分类名称
     * @return true or false
     */
    Boolean checkCategoryIsExist(String categoryName);
}
