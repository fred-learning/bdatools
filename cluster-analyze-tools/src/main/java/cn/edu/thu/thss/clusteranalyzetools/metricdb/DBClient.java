package cn.edu.thu.thss.clusteranalyzetools.metricdb;

import cn.edu.thu.thss.clusteranalyzetools.common.Config;
import cn.edu.thu.thss.clusteranalyzetools.common.DateUtil;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DBClient {
    private static Logger logger = Logger.getLogger(DBClient.class);
    private static Config config = Config.getInstance();
    private static Gson gson = new Gson();
    private MongoClient client;

    public boolean connect() {
        try {
            logger.info(String.format("Connect to mongo %s:%d",
                    config.getMongoIP(), config.getMongoPort()));
            client = new MongoClient(config.getMongoIP(), config.getMongoPort());
            createCollection();
            return true;
        } catch (MongoException e) {
            logger.warn("Connect to mongodb failed.\n", e);
            return false;
        }
    }

    public void close() {
        if (client != null) client.close();
    }

    private void createCollection() {
        MongoDatabase db = client.getDatabase(config.getMongoDBName());
        MongoCollection<Document> col = db.getCollection(config.getMongoCollectionName());
        if (col.count() == 0) {
            logger.info(String.format("Create mongo collection %s.%s",
                    config.getMongoDBName(), config.getMongoCollectionName()));
            col.dropIndexes();
            col.createIndex(new Document("createTime", 1));
        }
    }

    public void insertMetric(ClusterMetric metric) {
        Document doc = new Document();
        doc.put("createTime", DateUtil.getNowTime());
        doc.put("metric", gson.toJson(metric));
        logger.info("Insert metric data.");

        MongoDatabase db = client.getDatabase(config.getMongoDBName());
        MongoCollection<Document> col = db.getCollection(config.getMongoCollectionName());
        col.insertOne(doc);
    }

    public List<ClusterMetric> queryMetricByRange(Long start, Long end) {
        assert end > start;
        List<ClusterMetric> ret = new ArrayList<ClusterMetric>();

        Document criteria = new Document("createTime",
                new Document("$gt", start).append("$lt", end));
        MongoDatabase db = client.getDatabase(config.getMongoDBName());
        MongoCollection<Document> col = db.getCollection(config.getMongoCollectionName());
        FindIterable<Document> docs = col.find(criteria);
        for (Document doc : docs) {
            ClusterMetric metric = gson.fromJson(doc.getString("metric"), ClusterMetric.class);
            ret.add(metric);
        }

        return ret;
    }

    public static void main(String[] args) {
        ClusterMetric metric = MetricCrawler.getClusterMetric();
        DBClient client = new DBClient();
        client.connect();
        client.insertMetric(metric);
        List<ClusterMetric> ret = client.queryMetricByRange(DateUtil.getNowTime() - 1000000, DateUtil.getNowTime());
        logger.info(ret.size());
    }
}
