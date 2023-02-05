package cn.codeprobe.file.service;

import org.springframework.web.multipart.MultipartFile;

import cn.codeprobe.pojo.bo.NewAdminBO;

/**
 * @author Lionido
 */
public interface FileUploadService {

    /**
     * 上传 单张 图片到 OSS云存储
     *
     * @param userId 用户ID
     * @param file 待上传文件
     * @return imageUrl 上传文件的访问路径
     */
    String uploadImageToOss(String userId, MultipartFile file);

    /**
     * 上传 多张 图片到 OSS云存储
     *
     * @param userId 用户ID
     * @param file 待上传文件
     * @return imageUrl 上传文件的访问路径
     */
    String uploadImagesToOss(String userId, MultipartFile file);

    /**
     * 文件上传至 GridFS
     *
     * @param newAdminBO 包含着上传文件 img64
     * @return fileId 上传文件到GridFS后返回的ID
     */
    String uploadToGridFs(NewAdminBO newAdminBO);

}
