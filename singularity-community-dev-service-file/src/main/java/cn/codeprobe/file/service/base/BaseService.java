package cn.codeprobe.file.service.base;

import cn.codeprobe.file.utils.OssUtil;
import cn.codeprobe.file.utils.expand.FileResource;

import javax.annotation.Resource;

/**
 * @author Lionido
 */
public class BaseService {

    @Resource
    public OssUtil ossUtil;

    @Resource
    public FileResource fileResource;

    public static final String EXE_NAME_PNG = "png";
    public static final String EXE_NAME_JPG = "jpg";
    public static final String EXE_NAME_JPEG = "jpeg";

}
