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

	public JobData(String cid, int cstatus, JobConfig cconfig,
			JobResource climitedResource, Date cstartTime, Date cendTime,
			JournalData cjournalData, MonitoringData cmonitoringData,
			RDDSData crddsData) {
		id = cid;
		status = cstatus;
		config = cconfig;
		limitedResource = climitedResource;
		startTime = cstartTime;
		endTime = cendTime;
		journalData = cjournalData;
		monitoringData = cmonitoringData;
		rddsData = crddsData;
	}

	public double computeOneSimi(JobData other) {
		double sim_journal = journalData.similarity(other.journalData);
		double sim_monitor = monitoringData.similarity(other.monitoringData);
		double sim_rdds = rddsData.similarity(other.rddsData);
		double sum = sim_journal + sim_monitor + sim_rdds;
		System.out.println("journal simi:"+sim_journal+"-------rdds simi:"+sim_rdds);
		return sum;
	}

	/**
	 * Compute all similarity score between this and all instance in DB.
	 *
	 * @param db
	 *            , mongodb instance.
	 * @param k
	 *            , number of neighbors.
	 * @return k items. Each item are (sim between this and other, other
	 *         jobdata). Items are sorted in descending ordered.
	 */
	public List<TupleKeyComparable<Double, JobData>> computeAllSimi(
			DBInstance db, int k) {
		// min-heap, tuple for sim_score, application id
		PriorityQueue<TupleKeyComparable<Double, JobData>> Q = new PriorityQueue<TupleKeyComparable<Double, JobData>>();

		MongoCursor<Document> cursor = db.getMongoCollection().find()
				.iterator();
		try {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				JobData other = Util.mongodocToJobData(doc);
				// compute similarity, update heap
				double sim = this.computeOneSimi(other);
				Q.offer(new TupleKeyComparable<Double, JobData>(sim, other));
				if (Q.size() > k)
					Q.poll();

				// System.out.println("I found a saved job " + other.getId()
				// + ", simi " + sim);
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
		List<TupleKeyComparable<Double, JobData>> ret = new ArrayList<TupleKeyComparable<Double, JobData>>();
		for (int i = arr.length - 1; i >= 0; --i) {
			@SuppressWarnings("unchecked")
			TupleKeyComparable<Double, JobData> data = (TupleKeyComparable<Double, JobData>) arr[i];
			ret.add(data);
		}

		return ret;
	}

	public String getId() {
		return id;
	}

	public int getStatus() {
		return status;
	}

	public JobConfig getConfig() {
		return config;
	}

	public JobResource getLimitedResource() {
		return limitedResource;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public JournalData getJournalData() {
		return journalData;
	}

	public MonitoringData getMonitoringData() {
		return monitoringData;
	}

	public RDDSData getRddsData() {
		return rddsData;
	}

}
