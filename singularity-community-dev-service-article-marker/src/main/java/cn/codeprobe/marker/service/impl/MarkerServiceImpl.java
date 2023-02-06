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
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            gridFsBucket.downloadToStream(new ObjectId(mongoId), fileOutputStream);
            System.out.println(articleId);
            // close
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpStatus.OK.toString();
    }
}
