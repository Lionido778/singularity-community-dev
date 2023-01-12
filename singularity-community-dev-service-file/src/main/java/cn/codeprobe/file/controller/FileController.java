package cn.codeprobe.file.controller;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.file.FileControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.file.service.FileUploadService;
import cn.codeprobe.pojo.bo.NewAdminBO;
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
public class FileController extends ApiController implements FileControllerApi {

    @Resource
    private FileUploadService fileUploadService;

    @Override
    public JsonResult uploadFace(String userId, MultipartFile file) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 校验 file
        if (ObjectUtil.isEmpty(file) || file.getSize() == 0 || CharSequenceUtil.isBlank(file.getOriginalFilename())) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        // 文件访问路径
        String faceUrl = fileUploadService.uploadToOss(userId, file);
        return JsonResult.ok(faceUrl);
    }

    @Override
    public JsonResult uploadToGridFs(NewAdminBO newAdminBO) {
        // 校验 img64(人脸头像)
        String img64 = newAdminBO.getImg64();
        if (CharSequenceUtil.isBlank(img64) || "null".equalsIgnoreCase(img64)) {
            JsonResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }
        // 校验 username (管理员用户名)
        String username = newAdminBO.getUsername();
        if (CharSequenceUtil.isBlank(username)) {
            JsonResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }
        // 调用 service 上传 人脸数据
        String faceId = fileUploadService.uploadToGridFs(newAdminBO);
        return JsonResult.ok(faceId);
    }

    @Override
    public JsonResult readFromGridFs(String faceId) {
        //String img64 = fileSer
        return null;
    }
}
