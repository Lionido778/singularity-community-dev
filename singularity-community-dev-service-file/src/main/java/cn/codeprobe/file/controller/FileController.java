package cn.codeprobe.file.controller;

import cn.codeprobe.api.controller.file.FileControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalException;
import cn.codeprobe.file.service.FileUploadService;
import cn.codeprobe.result.JsonResult;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author Lionido
 */
@RestController
public class FileController implements FileControllerApi {

    @Resource
    private FileUploadService fileUploadService;

    @Override
    public JsonResult uploadFace(String userId, MultipartFile file) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalException.Internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 校验 file
        if (ObjectUtil.isEmpty(file) || file.getSize() == 0 || CharSequenceUtil.isBlank(file.getOriginalFilename())) {
            GlobalException.Internal(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        String faceUrl = "";
        // 文件访问路径
        faceUrl = fileUploadService.uploadToOss(userId, file);
        return JsonResult.ok(faceUrl);
    }
}
