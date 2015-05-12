package lab.paramcfg.backend.mongodb;

import java.util.Date;
import java.util.TreeMap;

import lab.paramcfg.backend.common.Util;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class JobData {

	private String id;
	private int status;
	private JobConfig config;
	private JobResource limitedResource;
	private Date startTime, endTime;

	private JournalData journalData;
	private MonitoringData monitoringData;
	private RDDSData rddsData;

	public JobData() {
		id = "-1";
	}

	public JobData(String id, int status, JobConfig config,
			JobResource limitedResource, Date startTime, Date endTime,
			JournalData journalData, MonitoringData monitoringData,
			RDDSData rddsData) {
		this.setId(id);
		this.setStatus(status);
		this.setConfig(config);
		this.setLimitedResource(limitedResource);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setJournalData(journalData);
		this.setMonitoringData(monitoringData);
		this.setRddsData(rddsData);
	}

	public double computeOneSimi(JobData other) {
		double sim_journal = journalData.similarity(other.journalData);
		double sim_monitor = monitoringData.similarity(other.monitoringData);
		double sim_rdds = rddsData.similarity(other.rddsData);
		double sum = sim_journal + sim_monitor + sim_rdds;
		return sum;
	}

	public JobData computeAllSimi(DBInstance db, int k) {
		JobData ret = new JobData();
		double simi = -1;
		MongoCollection<Document> collection = db.getMongoCollection();
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println("I found a saved job");
				Document doc = cursor.next();
				String id = doc.getString("id");
				int status = doc.getInteger("status");

				JobConfig config = Util.deserializeFromMongo(doc.get("config"),
						JobConfig.class);
				JobResource resource = Util.deserializeFromMongo(
						doc.get("limited_resource"), JobResource.class);
				Date startTime = doc.getDate("startTime");
				Date endTime = doc.getDate("endTime");
				JournalData jData = Util.deserializeFromMongo(
						doc.get("journal_data"), JournalData.class);
				MonitoringData mData = Util.deserializeFromMongo(
						doc.get("monitoring_data"), MonitoringData.class);
				RDDSData rData = Util.deserializeFromMongo(
						doc.get("rdds_data"), RDDSData.class);
				JobData tmpdata = new JobData(id, status, config, resource,
						startTime, endTime, jData, mData, rData);
				double tmpsimi = this.computeOneSimi(tmpdata);
				System.out.println(id+":it's simi is"+tmpsimi);
				if (tmpsimi > simi) {
					simi = tmpsimi;
					ret = tmpdata;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return ret;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public JobConfig getConfig() {
		return config;
	}

	public void setConfig(JobConfig config) {
		this.config = config;
	}

	public JobResource getLimitedResource() {
		return limitedResource;
	}

	public void setLimitedResource(JobResource limitedResource) {
		this.limitedResource = limitedResource;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public JournalData getJournalData() {
		return journalData;
	}

	public void setJournalData(JournalData journalData) {
		this.journalData = journalData;
	}

	public MonitoringData getMonitoringData() {
		return monitoringData;
	}

	public void setMonitoringData(MonitoringData monitoringData) {
		this.monitoringData = monitoringData;
	}

	public RDDSData getRddsData() {
		return rddsData;
	}

	public void setRddsData(RDDSData rddsData) {
		this.rddsData = rddsData;
	}

}
