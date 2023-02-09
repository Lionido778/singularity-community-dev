package cn.codeprobe.article.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import com.mongodb.client.gridfs.GridFSBucket;

import cn.codeprobe.article.mapper.ArticleMapper;
import cn.codeprobe.article.schedule.produceor.ScheduleService;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.Article;
import cn.codeprobe.pojo.vo.ArticleDetailVO;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.result.JsonResult;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Lionido
 */
public class CommonService {

    @Resource
    public ArticleMapper articleMapper;
    @Resource
    public GridFSBucket gridFsBucket;
    @Resource
    public ScheduleService scheduleService;
    @Resource
    public RestTemplate restTemplate;

    /**
     * 生成的静态文章页面上传至GridFS
     *
     * @param articleDetailVO 文章详情VO
     * @return mongoFileId
     */
    @Transactional(rollbackFor = Exception.class)
    public String generateHtmlToGridFs(ArticleDetailVO articleDetailVO) {
        String mongoFileId = null;
        try {
            // 配置freemarker基本环境
            Configuration cfg = new Configuration(Configuration.getVersion());
            // 声明freemarker模板所需要加载的目录的位置 (classpath:/templates)
            String classpath = this.getClass().getResource("/").getPath();
            System.out.println(classpath);
            cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));
            // eg:
            // D:/WorkSpace/ideaProjects/singularity-community-dev/singularity-community-dev-service-article/target/classes/templates
            // 获得现有的模板ftl文件
            Template template = cfg.getTemplate("detail.ftl", "utf-8");
            // 动态数据
            HashMap<String, Object> model = new HashMap<>(1);
            model.put("articleDetail", articleDetailVO);
            // 静态页面HTML,上传至GridFS
            String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            InputStream inputStream = IOUtils.toInputStream(htmlContent);
            ObjectId objectId = gridFsBucket.uploadFromStream(articleDetailVO.getId() + ".html", inputStream);
            // 关联至数据库文章表
            mongoFileId = objectId.toString();
            if (CharSequenceUtil.isBlank(mongoFileId)) {
                GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_FAILED);
            }
            Article article = new Article();
            article.setId(articleDetailVO.getId());
            article.setMongoFileId(mongoFileId);
            int result = articleMapper.updateByPrimaryKeySelective(article);
            if (result != MybatisResult.SUCCESS.result) {
                GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_FAILED);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_FAILED);
        }
        return mongoFileId;
    }

    /**
     * 获取用户基本信息
     *
     * @param userId 用户ID
     * @return UserBasicInfoVO
     */
    public UserBasicInfoVO getBasicUserInfoById(String userId) {
        String userServiceUrl = "http://www.codeprobe.cn:8003/writer/user/queryUserBasicInfo?userId=" + userId;
        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(userServiceUrl, JsonResult.class);
        JsonResult body = entity.getBody();
        UserBasicInfoVO userBasicInfoVO = null;
        if (body != null && body.getStatus() == HttpStatus.OK.value() && body.getData() != null) {
            Object data = body.getData();
            String jsonStr = JSONUtil.toJsonStr(data);
            userBasicInfoVO = JSONUtil.toBean(jsonStr, UserBasicInfoVO.class);
        }
        return userBasicInfoVO;
    }
}
