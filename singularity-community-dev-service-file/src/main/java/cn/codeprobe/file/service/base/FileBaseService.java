package cn.codeprobe.file.service.base;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.file.utils.OssUtil;
import cn.codeprobe.file.utils.expand.FileResource;
import com.mongodb.client.gridfs.GridFSBucket;
import org.springframework.web.multipart.MultipartFile;

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


    public String getFileExcName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String[] fileNameArr = fileName.split("\\.");
        // 这里需要转义\\.
        if (fileNameArr.length == 0) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        String imageExcName = fileNameArr[fileNameArr.length - 1];
        // 安全起见，限制只接收 PNG/JPG/JPEG 格式的文件
        if (!EXE_NAME_PNG.equalsIgnoreCase(imageExcName) && !EXE_NAME_JPG.equalsIgnoreCase(imageExcName)
                && !EXE_NAME_JPEG.equalsIgnoreCase(imageExcName)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FILE_FORMATTER_FAILED);
        }
        return imageExcName;
    }

}
