package lab.paramcfg.backend.mongodb;

import java.util.*;

import lab.paramcfg.backend.common.TupleKeyComparable;
import lab.paramcfg.backend.common.Util;

import org.bson.Document;

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

    /**
     * Compute all similarity score between this and all instance in DB.
     *
     * @param db, mongodb instance.
     * @param k, number of neighbors.
     * @return k items. Each item are (sim between this and other, other jobdata).
     *         Items are sorted in descending ordered.
     */
	public List<TupleKeyComparable<Double, JobData>> computeAllSimi(DBInstance db, int k) {
        // min-heap, tuple for sim_score, application id
        PriorityQueue<TupleKeyComparable<Double, JobData>> Q =
                new PriorityQueue<TupleKeyComparable<Double, JobData>>();

		MongoCursor<Document> cursor = db.getMongoCollection().find().iterator();
		try {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
                JobData other = Util.mongodocToJobData(doc);

                // compute similarity, update heap
				double sim = this.computeOneSimi(other);
                Q.offer(new TupleKeyComparable<Double, JobData>(sim, other));
                if (Q.size() > k) Q.poll();

                System.out.println("I found a saved job " + other.getId() + ", simi " + sim);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cursor.close();
		}

        // sort jobdata by descending similarity
        Object[] arr = Q.toArray();
        Arrays.sort(arr);
        List<TupleKeyComparable<Double, JobData>> ret =
                new ArrayList<TupleKeyComparable<Double, JobData>>();
        for (int i = arr.length - 1; i >= 0; -- i) {
            TupleKeyComparable<Double, JobData> data = (TupleKeyComparable<Double, JobData>) arr[i];
            ret.add(data);
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
