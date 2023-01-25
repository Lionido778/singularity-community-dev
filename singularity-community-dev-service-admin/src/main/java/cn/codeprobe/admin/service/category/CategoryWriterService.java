package cn.codeprobe.admin.service.category;

import java.util.List;

import cn.codeprobe.pojo.po.CategoryDO;

/**
 * @author Lionido
 */

public interface CategoryWriterService {

    /**
     * 创作中心 获取 category List
     *
     * @return list
     */
    List<CategoryDO> getCategories();

}
