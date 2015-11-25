package lab.paramcfg.backend.storage.others;


public class JobResource {
  
    private double memory_usage;
    private double executor_usage;
    
    public JobResource(double memory_usage, double executor_usage) {
       this.memory_usage = memory_usage;
       this.executor_usage = executor_usage;
    }
    
    public double getMemoryUsage() {
        return this.memory_usage;
    }
    
    public double getExecutorUsage() {
        return this.executor_usage;
    }

//    private int available_memory_in_megabyte;
//	private int available_vcores;
//
//	public JobResource(int memory_in_mb, int vcores) {
//		available_memory_in_megabyte = memory_in_mb;
//		available_vcores = vcores;
//	}
//
//	public JobResource() {
//		int[] ret = YarnUtils.pullYarnResource();
//		available_vcores = ret[0];
//		available_memory_in_megabyte = ret[1];
//	}
//
//	public int getAvailable_memory_in_megabyte() {
//		return available_memory_in_megabyte;
//	}
//
//	public int getAvailable_vcores() {
//		return available_vcores;
//	}

}
