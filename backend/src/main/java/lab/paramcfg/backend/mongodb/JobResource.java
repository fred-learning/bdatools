package lab.paramcfg.backend.mongodb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import lab.paramcfg.backend.common.Config;

import org.json.JSONObject;

public class JobResource implements Serializable {

	private int available_memory_in_megabyte;
	private int available_vcores;

	public JobResource(int memory_in_mb, int vcores) {
		setAvailable_memory_in_megabyte(memory_in_mb);
		setAvailable_vcores(vcores);
	}

	public JobResource() {
		int[] ret = YarnUtils.pullYarnResource();
		setAvailable_vcores(ret[0]);
		setAvailable_memory_in_megabyte(ret[1]);
	}

	public int getAvailable_memory_in_megabyte() {
		return available_memory_in_megabyte;
	}

	public void setAvailable_memory_in_megabyte(int available_memory_in_megabyte) {
		this.available_memory_in_megabyte = available_memory_in_megabyte;
	}

	public int getAvailable_vcores() {
		return available_vcores;
	}

	public void setAvailable_vcores(int available_vcores) {
		this.available_vcores = available_vcores;
	}

	
}
