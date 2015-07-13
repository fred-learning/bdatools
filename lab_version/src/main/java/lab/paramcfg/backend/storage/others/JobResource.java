package lab.paramcfg.backend.storage.others;

import java.io.Serializable;

public class JobResource {

    private int available_memory_in_megabyte;
	private int available_vcores;

	public JobResource(int memory_in_mb, int vcores) {
		available_memory_in_megabyte = memory_in_mb;
		available_vcores = vcores;
	}

	public JobResource() {
		int[] ret = YarnUtils.pullYarnResource();
		available_vcores = ret[0];
		available_memory_in_megabyte = ret[1];
	}

	public int getAvailable_memory_in_megabyte() {
		return available_memory_in_megabyte;
	}

	public int getAvailable_vcores() {
		return available_vcores;
	}

}
