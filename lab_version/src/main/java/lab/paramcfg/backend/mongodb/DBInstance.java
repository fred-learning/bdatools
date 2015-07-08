package lab.paramcfg.backend.mongodb;

import java.io.IOException;
import java.util.Date;

import lab.paramcfg.backend.common.Util;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.*;

public class DBInstance {

	private String connectHost;
	private String databaseName;
	private String collectionName;
	private MongoClient mongoClient;
	private MongoCollection<Document> mongoCollection;

	public DBInstance(String host, String dbName, String cName) {
		connectHost = host;
		databaseName = dbName;
		collectionName = cName;
		mongoClient = new MongoClient(host);
		mongoCollection = mongoClient.getDatabase(dbName).getCollection(cName);
	}

	public JobData getJobData(String jobid) throws ClassNotFoundException,
			IOException {
		Document doc = mongoCollection.find(eq("id", jobid)).first();
		if (doc == null)
			return null;

		String id = doc.getString("id");
		int status = doc.getInteger("status");

		JobConfig config = Util.deserializeFromMongo(doc.get("config"),
				JobConfig.class);
		JobResource resource = Util.deserializeFromMongo(
				doc.get("limited_resource"), JobResource.class);
		Date startTime = doc.getDate("startTime");
		Date endTime = doc.getDate("endTime");
		JournalData jData = Util.deserializeFromMongo(doc.get("journal_data"),
				JournalData.class);
		MonitoringData mData = Util.deserializeFromMongo(
				doc.get("monitoring_data"), MonitoringData.class);
		RDDSData rData = Util.deserializeFromMongo(doc.get("rdds_data"),
				RDDSData.class);

		return new JobData(id, status, config, resource, startTime, endTime,
				jData, mData, rData);
	}

	public void saveJobData(String id, int status, JobConfig config,
			JobResource limitedResource, Date startTime, Date endTime,
			JournalData journalData, MonitoringData monitoringData,
			RDDSData rddsData) throws IOException {
		// serialize non structure data
		Document doc = new Document()
				.append("id", id)
				.append("status", status)
				.append("config",
						Util.serializeToMongo(config, JobConfig.class))
				.append("limited_resource",
						Util.serializeToMongo(limitedResource,
								JobResource.class))
				.append("startTime", startTime)
				.append("endTime", endTime)
				.append("journal_data",
						Util.serializeToMongo(journalData, JournalData.class))
				.append("monitoring_data",
						Util.serializeToMongo(monitoringData,
								MonitoringData.class))
				.append("rdds_data",
						Util.serializeToMongo(rddsData, RDDSData.class));
		getMongoCollection().insertOne(doc);
	}

	public void clearCollection() {
		getMongoCollection().drop();
	}

	public void close() {
		getMongoClient().close();
	}

	public String getConnectHost() {
		return connectHost;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public MongoCollection<Document> getMongoCollection() {
		return mongoCollection;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}
}
