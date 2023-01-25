package cn.codeprobe.file.controller;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.api.controller.file.FileWriterControllerApi;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.file.service.FileDownloadService;
import cn.codeprobe.file.service.FileUploadService;
import cn.codeprobe.result.JsonResult;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author Lionido
 */
@RestController
public class FileWriterController extends ApiController implements FileWriterControllerApi {

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
