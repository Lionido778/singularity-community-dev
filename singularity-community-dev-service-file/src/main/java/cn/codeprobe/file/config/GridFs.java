package cn.codeprobe.file.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lionido
 */
@Configuration
public class GridFs {

    @Value("${spring.data.mongodb.database}")
    private String mongodbDatabaseName;

    @Bean
    public GridFSBucket gridFsBucket(MongoClient mongoClient) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(mongodbDatabaseName);
        return GridFSBuckets.create(mongoDatabase);
    }
}
