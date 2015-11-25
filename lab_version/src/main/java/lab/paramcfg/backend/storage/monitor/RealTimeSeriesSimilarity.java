package lab.paramcfg.backend.storage.monitor;

import java.util.List;

public interface RealTimeSeriesSimilarity {

	public double calculateSimilarity(List<List<Double>> timeSeries,List<List<Double>> compareTimeSeries);
}
