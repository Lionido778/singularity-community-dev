package cn.codeprobe.file.service.impl;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalException;
import cn.codeprobe.file.service.FileUploadService;
import cn.codeprobe.file.service.base.BaseService;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lionido
 */
@Service
public class FileUploadServiceImpl extends BaseService implements FileUploadService {

    @Override
    public String uploadToOss(String userId, MultipartFile file) {
        // 获取用户上传的文件扩展名
        String fileName = file.getOriginalFilename();
        if (CharSequenceUtil.isBlank(fileName)) {
            GlobalException.Internal(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        // 这里需要转义\\.
        String[] fileNameArr = fileName.split("\\.");
        if (fileNameArr.length == 0) {
            GlobalException.Internal(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        String fileExcName = fileNameArr[fileNameArr.length - 1];
        // 安全起见，只接收 PNG/JPG/JPEG 格式的文件
        if (!EXE_NAME_PNG.equalsIgnoreCase(fileExcName) && !EXE_NAME_JPG.equalsIgnoreCase(fileExcName)
                && !EXE_NAME_JPEG.equalsIgnoreCase(fileExcName)) {
            GlobalException.Internal(ResponseStatusEnum.FILE_FORMATTER_FAILED);
        }
        // 上传头像
        String storagePath = ossUtil.uploadToOss(userId, file, fileExcName);
        // 返回可访问路径
        return fileResource.getHost() + storagePath;
    }

}
