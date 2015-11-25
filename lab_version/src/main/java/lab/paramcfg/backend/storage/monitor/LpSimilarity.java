package lab.paramcfg.backend.storage.monitor;

import java.util.List;

public class LpSimilarity implements RealTimeSeriesSimilarity {

	public double calculateSimilarity(List<List<Double>> timeSeries,List<List<Double>> compareTimeSeries) {
	  
		double distance = 0;
		for(int i = 0 ; i < timeSeries.size(); i ++){
			
			List<Double> timeSerie = timeSeries.get(i);
			List<Double> compareTimeSerie = compareTimeSeries.get(i);
			
			for(int j = 0 ; j < (timeSerie.size() > compareTimeSerie.size() ? compareTimeSerie.size() : timeSerie.size()); j ++){
				
				distance += Math.pow(timeSerie.get(j)-compareTimeSerie.get(j),2);
			}
			
			
		}
		distance = Math.sqrt(distance);
		return distance;
	}

}
