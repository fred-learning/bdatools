package historydb;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import common.Config;
import org.apache.log4j.Logger;
import org.bson.Document;

public class Client {
    private static Logger logger = Logger.getLogger(Client.class);
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
        col = db.getCollection(conf.getMongoDBCollection());
        if (col.count() == 0) {
            col.dropIndexes();
            logger.info("Create mongodb collection " + conf.getMongoDBCollection());
            col.createIndex(new Document("appid", 1), new IndexOptions().unique(true));
        }
    }

    public void insertApp(App app) {
        Document doc = new Document();
        doc.put("appid", app.getAppid());
        doc.put("app", gson.toJson(app));

        logger.info("Insert application " + app.getAppid() + " into history database.");
        col.insertOne(doc);
    }

    public App findApp(String appid) {
        FindIterable<Document> docs = col.find(new Document("appid", appid));
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
}
