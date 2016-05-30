package historydb;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import common.Config;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class HistoryClient {
    private static Logger logger = Logger.getLogger(HistoryClient.class);
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

    private void createCollection() {
        MongoDatabase db = client.getDatabase(conf.getMongoDBName());
        col = db.getCollection(conf.getMongoHistoryCollection());
        if (col.count() == 0) {
            col.dropIndexes();
            logger.info("Create mongodb collection " + conf.getMongoHistoryCollection());
            col.createIndex(new Document("appid", 1), new IndexOptions().unique(true));
            col.createIndex(new Document("clusterid", 1));
        }
    }

    public void insertApp(App app) {
        Document src = new Document().append("appid", app.getAppid())
                                     .append("clusterid", app.getClusterid());
        Document target = new Document("$set", new Document("app", gson.toJson(app)));
        logger.info("Insert application " + app.getAppid() + " into history database.");
        col.updateMany(src, target, new UpdateOptions().upsert(true));
    }

    public void deleteApp(String appid) {
        Document doc = new Document();
        doc.put("appid", appid);
        doc.put("clusterid", conf.getClusterID());
        col.deleteOne(doc);
    }

    public App findApp(String clusterid, String appid) {
        appid = appid.trim();
        Document searchDoc = new Document().append("appid", appid).append("clusterid", clusterid);
        FindIterable<Document> docs = col.find(searchDoc);
        Document doc = docs.first();
        if (doc != null) {
            App app = gson.fromJson(doc.getString("app"), App.class);
            return app;
        } else {
            return null;
        }
    }

    public AppIterator getIterator() {
        return new AppIterator(col);
    }

    public List<App> getItems(int pageIdx) {
        final int NUM = conf.getMongoNumItemsPerPage();
        Document sortDoc = new Document().append("_id", -1);
        FindIterable<Document> docs = col.find().sort(sortDoc).skip(pageIdx * NUM).limit(NUM);
        List<App> ret = new ArrayList<App>();
        for (Document doc : docs) {
            App app = gson.fromJson(doc.getString("app"), App.class);
            ret.add(app);
        }
        return ret;
    }

    public int pageCount() {
        return Math.max((int) Math.ceil(1.0 * col.count() / conf.getMongoNumItemsPerPage()), 1);
    }
}
