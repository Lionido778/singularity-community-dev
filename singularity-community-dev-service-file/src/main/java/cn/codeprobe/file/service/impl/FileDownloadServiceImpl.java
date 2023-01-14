package cn.codeprobe.file.service.impl;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.file.service.FileDownloadService;
import cn.codeprobe.file.service.base.FileBaseService;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author Lionido
 */
@Service
public class FileDownloadServiceImpl extends FileBaseService implements FileDownloadService {
    @Override
    public File downloadFileFromGridFs(String faceId) {

        GridFSFindIterable gridFsFindIterable = gridFsBucket
                .find(Filters.eq("_id", new ObjectId(faceId)));
        // 获取文件
        GridFSFile gridFsFile = gridFsFindIterable.first();
        String filename = "null";
        if (gridFsFile != null) {
            filename = gridFsFile.getFilename();
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        System.out.println("查看的人脸图像名称：" + filename);
        // 获取文件流，将文件下载到本地或服务器的临时目录
        File fileTemp = new File(FILE_TEMP);
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        File file = new File(FILE_TEMP + filename);
        try {
            //  创建文件输出流
            FileOutputStream os = new FileOutputStream(file);
            // 将文件输出到临时目录
            gridFsBucket.downloadToStream(new ObjectId(faceId), os);
        } catch (FileNotFoundException e) {
            GlobalExceptionManage.internal(ResponseStatusEnum.FILE_DOWNLOAD_ERROR);
        }
        return file;
    }
}
