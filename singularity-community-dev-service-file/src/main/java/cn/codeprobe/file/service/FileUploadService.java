package cn.codeprobe.file.service;

import cn.codeprobe.pojo.bo.NewAdminBO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lionido
 */
public interface FileUploadService {

    /**
     * 上传单个图片到 OSS云存储
     *
     * @param userId 用户ID
     * @param file   待上传文件
     * @return faceUlr    上传头像的访问路径
     */
    String uploadImageToOss(String userId, MultipartFile file);

    /**
     * 上传多个图片到 OSS云存储
     *
     * @param userId 用户ID
     * @param file   待上传文件
     * @return faceUlr    上传头像的访问路径
     */
    String uploadImagesToOss(String userId, MultipartFile file);


    /**
     * 文件上传至 GridFS
     *
     * @param newAdminBO 包含着图像上传信息
     * @return fileId 上传文件ID
     */
    String uploadToGridFs(NewAdminBO newAdminBO);
}
