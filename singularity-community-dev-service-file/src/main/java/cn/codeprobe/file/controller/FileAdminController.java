package cn.codeprobe.file.controller;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.file.FileAdminControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.file.service.FileDownloadService;
import cn.codeprobe.file.service.FileUploadService;
import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.utils.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class FileAdminController extends ApiController implements FileAdminControllerApi {

    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private FileDownloadService fileDownloadService;

    @Override
    public JsonResult uploadToGridFs(NewAdminBO newAdminBO) {
        // 校验 img64(人脸头像)
        String img64 = newAdminBO.getImg64();
        if (CharSequenceUtil.isBlank(img64)) {
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
    public void readFaceFromGridFs(String faceId) {
        // 校验 faceId
        if (CharSequenceUtil.isBlank(faceId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        // 调用 service 获取人脸文件
        File faceFile = fileDownloadService.downloadFileFromGridFs(faceId);
        // 将人脸图像响应到前端
        FileUtil.downloadFileByStream(response, faceFile);
    }

    @Override
    public JsonResult readBase64FaceFromGridFs(String faceId) {
        // 校验 faceId
        if (CharSequenceUtil.isBlank(faceId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        // 调用 service 获取人脸数据文件
        File faceFile = fileDownloadService.downloadFileFromGridFs(faceId);
        // 返回 Base64
        String base64 = FileUtil.fileToBase64(faceFile);
        return JsonResult.ok(base64);
    }

}
