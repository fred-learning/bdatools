package lab.paramcfg.backend.storage.monitor;
import java.util.List;
public interface RealTimeSeriesRepresentation {

	public List<List<Double>> getRealRepresentation(List<List<Double>> timeSeries,ParametersObject paramtersObject);
	
}
