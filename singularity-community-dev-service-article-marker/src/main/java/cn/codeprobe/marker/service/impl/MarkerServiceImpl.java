package cn.codeprobe.marker.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mongodb.client.gridfs.GridFSBucket;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.marker.service.MarkerService;

/**
 * @author Lionido
 */
@Service
public class MarkerServiceImpl implements MarkerService {

    @Resource
    private GridFSBucket gridFsBucket;
    @Value("${freemarker.html.target}")
    private String htmlTargetPath;

    @Override
    public String publishHtml(String articleId, String mongoId) {
        try {
            String path = htmlTargetPath + File.separator + articleId + ".html";
            File tempDic = new File(htmlTargetPath);
            if (!tempDic.exists()) {
                tempDic.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            gridFsBucket.downloadToStream(new ObjectId(mongoId), fileOutputStream);
            // close
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpStatus.OK.toString();
    }

    @Override
    public void publishHtmlByMq(String articleId, String mongoId) {
        try {
            String path = htmlTargetPath + File.separator + articleId + ".html";
            File tempDic = new File(htmlTargetPath);
            if (!tempDic.exists()) {
                tempDic.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            gridFsBucket.downloadToStream(new ObjectId(mongoId), fileOutputStream);
            // close
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionManage.internal(ResponseStatusEnum.RABBITMQ_CONSUMER_DOWNLOAD_ERROR);
        }
    }

    @Override
    public String deleteHtml(String articleId) {
        String path = htmlTargetPath + File.separator + articleId + ".html";
        File file = new File(path);
        boolean result = file.delete();
        if (result) {
            return HttpStatus.OK.toString();
        }
        return null;
    }

    @Override
    public void deleteHtmlByMq(String articleId) {
        String path = htmlTargetPath + File.separator + articleId + ".html";
        File file = new File(path);
        boolean result = file.delete();
        if (!result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.RABBITMQ_CONSUMER_DELETE_ERROR);
        }
    }
}
