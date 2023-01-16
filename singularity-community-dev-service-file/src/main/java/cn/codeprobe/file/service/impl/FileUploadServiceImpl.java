package cn.codeprobe.file.service.impl;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.file.service.FileUploadService;
import cn.codeprobe.file.service.base.FileBaseService;
import cn.codeprobe.pojo.bo.NewAdminBO;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Lionido
 */
@Service
public class FileUploadServiceImpl extends FileBaseService implements FileUploadService {

    @Override
    public String uploadImagesToOss(String userId, MultipartFile file) {
        // 获取用户上传的文件扩展名
        String imageExcName = getFileExcName(file);
        // 上传文件
        String articlePath = fileResource.getArticlePath();
        String storagePath = ossUtil.uploadToOss(userId, file, imageExcName, articlePath);
        // 返回可访问路径
        return fileResource.getHost() + storagePath;
    }

    @Override
    public String uploadImageToOss(String userId, MultipartFile file) {
        // 获取用户上传的文件扩展名
        String imageExcName = getFileExcName(file);
        // 上传头像
        String facePath = fileResource.getFacePath();
        String storagePath = ossUtil.uploadToOss(userId, file, imageExcName, facePath);
        // 返回可访问路径
        return fileResource.getHost() + storagePath;
    }

    @Override
    public String uploadToGridFs(NewAdminBO newAdminBO) {
        // 获取图像的 Base64 字符串
        String img64 = newAdminBO.getImg64();
        String username = newAdminBO.getUsername();
        byte[] bytes = null;
        try {
            // Base64 字符串转换为 byte数组
            bytes = new BASE64Decoder().decodeBuffer(img64);
        } catch (IOException e) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FILE_UPLOAD_FAILED);
        }
        // byte数组 转换为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        // 生成图像名称
        String imageName = username + "." + EXE_NAME_PNG;
        // 文件上传 至 GridFS，并获取主键 ID
        ObjectId objectId = gridFsBucket.uploadFromStream(imageName, inputStream);
        // 返回 face ID 主键
        return objectId.toString();
    }

}
