package cn.codeprobe.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lionido
 */
public interface FileUploadService {

    /**
     * 文件上传到OSS云存储
     *
     * @param userId 用户ID
     * @param file   待上传文件
     * @return faceUlr    上传头像的访问路径
     */
    String uploadToOss(String userId, MultipartFile file);
}
