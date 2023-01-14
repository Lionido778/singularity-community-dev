package cn.codeprobe.file.service;

import java.io.File;

/**
 * @author Lionido
 */
public interface FileDownloadService {

    /**
     * 从GridFS中下载文件
     *
     * @param id 主键
     */
    File downloadFileFromGridFs(String id);
}
