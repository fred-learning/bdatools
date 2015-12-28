package server.service;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import common.Config;
import common.DateUtil;
import org.apache.log4j.Logger;
import org.bson.Document;

public class ProgressClient {
    private static Logger logger = Logger.getLogger(ProgressClient.class);
    private static Config conf = Config.getInstance();
    private MongoClient client;
    private MongoCollection<Document> col;

    public void connect() {
        if (client == null) {
            try {
                logger.info(String.format("Connect to mongo ip addr: %s:%s",
                        conf.getMongoIP(), conf.getMongoPort()));
                client = new MongoClient(conf.getMongoIP(), Integer.parseInt(conf.getMongoPort()));
                createCollection();
                resetState();
            } catch (MongoException e) {
                logger.fatal("Connect to mongodb failed.\n", e);
                logger.fatal("System exit!");
                System.exit(-1);
            }
        }
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }

    public void setRunning(String progressid, String appid) {
        Document doc = new Document()
                .append("progressid", progressid)
                .append("clusterid", conf.getClusterID())
                .append("appid", appid)
                .append("startTime", DateUtil.getNowTimeStr())
                .append("endTime", "")
                .append("status", "running")
                .append("result", "");
        col.insertOne(doc);
    }

    public void setFinished(String progressid, String result) {
        Document queryDoc = new Document("progressid", progressid);
        Document updateDoc = new Document("$set",
                new Document("endTime", DateUtil.getNowTimeStr())
                        .append("result", result)
                        .append("status", "finished"));
        col.updateOne(queryDoc, updateDoc);
    }

    public void setError(String progressid, String errorMsg) {
        Document queryDoc = new Document("progressid", progressid);
        Document updateDoc = new Document("$set",
                new Document("endTime", DateUtil.getNowTimeStr())
                        .append("result", errorMsg)
                        .append("status", "error"));
        col.updateOne(queryDoc, updateDoc);
    }

    private void createCollection() {
        MongoDatabase db = client.getDatabase(conf.getMongoDBName());
        col = db.getCollection(conf.getMongoRecommendProgressCollection());
        if (col.count() == 0) {
            col.dropIndexes();
            logger.info("Create mongodb collection " + conf.getMongoRecommendProgressCollection());
            col.createIndex(new Document("startTime", 1));
            col.createIndex(new Document("progressid", 1), new IndexOptions().unique(true));
            col.createIndex(new Document("status", 1));
        }
    }

    private void resetState() {
        Document queryDoc = new Document("status", "running").append("clusterid", conf.getClusterID());
        Document updateDoc = new Document("$set",
                new Document("endTime", DateUtil.getNowTimeStr())
                        .append("result", "{\"errMsg\": \"System exit.\"}")
                        .append("status", "error"));
        col.updateMany(queryDoc, updateDoc);
    }

}
