package cn.codeprobe.file.controller;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.file.FileControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.file.service.FileDownloadService;
import cn.codeprobe.file.service.FileUploadService;
import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.utils.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * @author Lionido
 */
@RestController
public class FileController extends ApiController implements FileControllerApi {

    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private FileDownloadService fileDownloadService;


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
        String faceUrl = fileUploadService.uploadImageToOss(userId, file);
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
    public void readFaceFromGridFs(String faceId) throws FileNotFoundException {
        // 校验 faceId
        if (CharSequenceUtil.isBlank(faceId) || "null".equalsIgnoreCase(faceId)) {
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

    @Override
    public JsonResult uploadSerialsFiles(String userId, MultipartFile[] files) {
        // 校验 userId
        if (CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.UN_LOGIN);
        }
        // 校验 files
        int length = files.length;
        if (length == 0) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        ArrayList<String> list = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = "";
            try {
                url = fileUploadService.uploadImagesToOss(userId, file);
            } catch (Exception e) {
                e.getStackTrace();
                continue;
            }
            list.add(url);
        }
        return JsonResult.ok(list);
    }
}
