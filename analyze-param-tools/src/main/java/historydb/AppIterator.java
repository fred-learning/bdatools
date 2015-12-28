package historydb;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.Iterator;

public class AppIterator implements Iterator<App> {
    private static Logger logger = Logger.getLogger(AppIterator.class);
    private static Gson gson = new Gson();
    private MongoCursor<Document> iterator;

    public AppIterator(MongoCollection<Document> apps) {
        logger.info("Open app iterator.");
        this.iterator = apps.find().iterator();
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.iterator != null) {
            logger.info("Close app iterator.");
            this.iterator.close();
        }
        super.finalize();
    }

    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    public App next() {
        Document doc = iterator.next();
        return gson.fromJson(doc.getString("app"), App.class);
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
