package cn.codeprobe.file.service.base;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.file.utils.OssUtil;
import cn.codeprobe.file.utils.expand.FileResource;
import com.mongodb.client.gridfs.GridFSBucket;

import javax.annotation.Resource;

/**
 * @author Lionido
 */
public class FileBaseService extends ApiController {

    @Resource
    public OssUtil ossUtil;
    @Resource
    public GridFSBucket gridFsBucket;
    @Resource
    public FileResource fileResource;

    public static final String EXE_NAME_PNG = "png";
    public static final String EXE_NAME_JPG = "jpg";
    public static final String EXE_NAME_JPEG = "jpeg";


    public static final String FILE_TEMP = "/workspace/grid_fs/";


}
