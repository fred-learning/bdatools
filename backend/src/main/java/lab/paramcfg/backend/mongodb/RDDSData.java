package lab.paramcfg.backend.mongodb;

import java.io.Serializable;

public class RDDSData implements Serializable {
  
  private String jobid;
  
  public RDDSData(String jobid) {
    this.jobid = jobid;
  }
  
  public double similarity(RDDSData other) {
    return 0;
  }

public String getJobid() {
	return jobid;
}

public void setJobid(String jobid) {
	this.jobid = jobid;
}
  
}
