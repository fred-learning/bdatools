package lab.paramcfg.backend.storage.monitor;

import java.util.List;

public class SAXConstructObject {

	private List<List<Double>> timeSeries1;
	private List<List<Double>> timeSeries2;
	private List<List<Double>> transferTimeSeries1;
	private List<List<Double>> transferTimeSeries2;
	private ParametersObject saxParametersObject;
	
	
	
	
	public ParametersObject getSaxParametersObject() {
		return saxParametersObject;
	}
	public void setSaxParametersObject(ParametersObject saxParametersObject) {
		this.saxParametersObject = saxParametersObject;
	}
	private double distance ;
	
	public List<List<Double>> getTimeSeries1() {
		return timeSeries1;
	}
	public void setTimeSeries1(List<List<Double>> timeSeries1) {
		this.timeSeries1 = timeSeries1;
	}
	public List<List<Double>> getTimeSeries2() {
		return timeSeries2;
	}
	public void setTimeSeries2(List<List<Double>> timeSeries2) {
		this.timeSeries2 = timeSeries2;
	}
	public List<List<Double>> getTransferTimeSeries1() {
		return transferTimeSeries1;
	}
	public void setTransferTimeSeries1(List<List<Double>> transferTimeSeries1) {
		this.transferTimeSeries1 = transferTimeSeries1;
	}
	public List<List<Double>> getTransferTimeSeries2() {
		return transferTimeSeries2;
	}
	public void setTransferTimeSeries2(List<List<Double>> transferTimeSeries2) {
		this.transferTimeSeries2 = transferTimeSeries2;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
}
