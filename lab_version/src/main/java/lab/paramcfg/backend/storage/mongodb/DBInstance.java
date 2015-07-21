package lab.paramcfg.backend.storage.mongodb;

import java.io.IOException;
import java.util.Date;

import lab.paramcfg.backend.storage.JobData;
import lab.paramcfg.backend.storage.graph.RDDSData;
import lab.paramcfg.backend.storage.journal.JournalData;
import lab.paramcfg.backend.storage.monitor.MonitoringData;
import lab.paramcfg.backend.storage.others.JobConfig;
import lab.paramcfg.backend.storage.others.JobResource;

import org.bson.Document;

import com.google.gson.Gson;
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

//		JobConfig config = Util.deserializeFromMongo(doc.get("config"),
//				JobConfig.class);
//		JobResource resource = Util.deserializeFromMongo(
//				doc.get("limited_resource"), JobResource.class);
		Date startTime = doc.getDate("startTime");
		Date endTime = doc.getDate("endTime");
//		JournalData jData = Util.deserializeFromMongo(doc.get("journal_data"),
//				JournalData.class);
//		MonitoringData mData = Util.deserializeFromMongo(
//				doc.get("monitoring_data"), MonitoringData.class);
//		RDDSData rData = Util.deserializeFromMongo(doc.get("rdds_data"),
//				RDDSData.class);
		
		Gson gson = new Gson();
		JobConfig config = gson.fromJson(doc.getString("config"), JobConfig.class);
		JobResource resource = gson.fromJson(doc.getString("limited_resource"), JobResource.class);
		JournalData jData = gson.fromJson(doc.getString("journal_data"), JournalData.class);
		MonitoringData mData = gson.fromJson(doc.getString("monitoring_data"), MonitoringData.class);
		RDDSData rData = gson.fromJson(doc.getString("rdds_data"), RDDSData.class);

		return new JobData(id, status, config, resource, startTime, endTime,
				jData, mData, rData);
	}

	public void saveJobData(String id, int status, JobConfig config,
			JobResource limitedResource, Date startTime, Date endTime,
			JournalData journalData, MonitoringData monitoringData,
			RDDSData rddsData) throws IOException {
	    Gson gson = new Gson();
		// serialize non structure data
		Document doc = new Document()
				.append("id", id)
				.append("status", status)
				.append("config",
				        gson.toJson(config, JobConfig.class))
				.append("limited_resource",
						gson.toJson(limitedResource, JobResource.class))
				.append("startTime", startTime)
				.append("endTime", endTime)
				.append("journal_data",
				        gson.toJson(journalData, JournalData.class))
				.append("monitoring_data",
				        gson.toJson(monitoringData, MonitoringData.class))
				.append("rdds_data",
				        gson.toJson(rddsData, RDDSData.class));
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
