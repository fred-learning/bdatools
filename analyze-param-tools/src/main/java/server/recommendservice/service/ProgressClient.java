package server.recommendservice.service;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import common.Config;
import common.DateUtil;
import org.apache.log4j.Logger;
import org.bson.Document;
import server.recommendservice.response.ProgressViewItem;

import java.util.ArrayList;
import java.util.List;

public class ProgressClient {
    private static Logger logger = Logger.getLogger(ProgressClient.class);
    private static Config conf = Config.getInstance();
    private static Gson gson = new Gson();
    private MongoClient client;
    private MongoCollection<Document> col;

    public void connect() {
        if (client == null) {
            try {
                logger.info(String.format("Connect to mongo ip addr: %s:%s",
                        conf.getMongoIP(), conf.getMongoPort()));
                client = new MongoClient(conf.getMongoIP(), Integer.parseInt(conf.getMongoPort()));
                createCollection();
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

    public void resetState() {
        assert client != null;
        try {
            Document queryDoc = new Document("status", "running").append("clusterid", conf.getClusterID());
            Document updateDoc = new Document("$set",
                    new Document("endTime", DateUtil.getNowTimeStr())
                            .append("result", "{\"errMsg\": \"System exit.\"}")
                            .append("status", "error"));
            col.updateMany(queryDoc, updateDoc);
        } catch (MongoException e) {
            logger.fatal("Reset mongodb state failed.\n", e);
            logger.fatal("System exit!");
            System.exit(-1);
        }
    }

    public List<ProgressViewItem> getItems(int pageIdx) {
        final int NUM = conf.getMongoNumItemsPerPage();
        Document sortDoc = new Document().append("_id", -1);
        FindIterable<Document> docs = col.find().sort(sortDoc).skip(pageIdx * NUM).limit(NUM);

        List<ProgressViewItem> ret = new ArrayList<ProgressViewItem>();
        for (Document doc : docs) {
            String progressid = doc.getString("progressid");
            String clusterid = doc.getString("clusterid");
            String appid = doc.getString("appid");
            String startTime = doc.getString("startTime");
            String endTime = doc.getString("endTime");
            String status = doc.getString("status");
            String result = doc.getString("result");
            ProgressViewItem item = new ProgressViewItem(progressid, clusterid, appid,
                    startTime, endTime, status, result);
            ret.add(item);
        }
        return ret;
    }

    public ProgressViewItem getItem(String progressid) {
        Document searchDoc = new Document().append("progressid", progressid);
        FindIterable<Document> docs = col.find(searchDoc);
        Document doc = docs.first();
        if (doc != null) {
            String clusterid = doc.getString("clusterid");
            String appid = doc.getString("appid");
            String startTime = doc.getString("startTime");
            String endTime = doc.getString("endTime");
            String status = doc.getString("status");
            String result = doc.getString("result");
            ProgressViewItem item = new ProgressViewItem(progressid, clusterid, appid,
                    startTime, endTime, status, result);
            return item;
        } else {
            return null;
        }
    }

    public int pageCount() {
        return Math.max((int) Math.ceil(1.0 * col.count() / conf.getMongoNumItemsPerPage()), 1);
    }
}
