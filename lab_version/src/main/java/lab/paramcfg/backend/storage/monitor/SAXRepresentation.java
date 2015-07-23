package lab.paramcfg.backend.storage.monitor;

import java.util.ArrayList;
import java.util.List;

public class SAXRepresentation implements RealTimeSeriesRepresentation{
	
	public static List<Double> mapToString(List<Double> PAA,int alphabet_size){
		
		List<Double> string = new ArrayList<Double>();
		double[] cut_points = {};
		
		switch(alphabet_size){
		
			case 2: cut_points = new double[]{-Double.MAX_VALUE,0};break;
			case 3: cut_points = new double[]{-Double.MAX_VALUE,-0.43,0.43};break;
			case 4: cut_points = new double[]{-Double.MAX_VALUE,-0.67,0,0.67};break;
			case 5: cut_points = new double[]{-Double.MAX_VALUE,-0.84,-0.25,0.25,0.84};break;
			case 6: cut_points = new double[]{-Double.MAX_VALUE,-0.97,-0.43,0,0.43,0.97};break;
			case 7: cut_points = new double[]{-Double.MAX_VALUE,-1.07,-0.57,-0.18,0.18,0.57,1.07};break;
			case 8: cut_points = new double[]{-Double.MAX_VALUE,-1.15,-0.67,-0.32,0,0.32,0.67,1.15};break;
			case 9: cut_points = new double[]{-Double.MAX_VALUE,-1.22,-0.76,-0.43,-0.14,0.14,0.43,0.76,1.22};break;
			case 10: cut_points = new double[]{-Double.MAX_VALUE,-1.28,-0.84,-0.52,-0.25,0,0.25,0.52,0.84,1.28};break;
			case 11: cut_points = new double[]{-Double.MAX_VALUE,-1.34,-0.91,-0.6,-0.35,-0.11,0.11,0.35,0.6,0.91,1.34};break;
			case 12: cut_points = new double[]{-Double.MAX_VALUE,-1.38,-0.97,-0.67,-0.43,-0.21,0,0.21,0.43,0.67,0.97,1.38};break;
			case 13: cut_points = new double[]{-Double.MAX_VALUE,-1.43,-1.02,-0.74,-0.5,-0.29,-0.1,0.1,0.29,0.5,0.74,1.02,1.43};break;
			case 14: cut_points = new double[]{-Double.MAX_VALUE,-1.47,-1.07,-0.79,-0.57,-0.37,-0.18,0,0.18,0.37,0.57,0.79,1.07,1.47};break;
			case 15: cut_points = new double[]{-Double.MAX_VALUE,-1.5,-1.11,-0.84,-0.62,-0.43,-0.25,-0.08,0.08,0.25,0.43,0.62,0.84,1.11,1.5};break;
			case 16: cut_points = new double[]{-Double.MAX_VALUE,-1.53,1.15,-0.89,-0.67,-0.49,-0.32,-0.16,0,0.16,0.32,0.49,0.67,0.89,1.15,1.53};break;
			case 17: cut_points = new double[]{-Double.MAX_VALUE,-1.56,-1.19,-0.93,-0.72,-0.54,-0.38,0.22,-0.07,0.07,0.22,0.38,0.54,0.72,0.93,1.19,1.56};break;
			case 18: cut_points = new double[]{-Double.MAX_VALUE,-1.59,-1.22,-0.97,-0.76,-0.59,-0.43,-0.28,-0.14,0.14,0.28,0.43,0.59,0.76,0.97,1.22,1.59};break;
			case 19: cut_points = new double[]{-Double.MAX_VALUE,-1.62,-1.25,-1,-0.8,-0.63,-0.48,-0.34,-0.2,-0.07,0.07,0.2,0.34,0.48,0.63,0.8,1,1.25,1.62};break;
			case 20: cut_points = new double[]{-Double.MAX_VALUE,-1.64,-1.28,-1.04,-1.04,-0.84,-0.67,-0.52,-0.39,-0.25,-0.13,0,0.13,0.25,0.39,0.67,0.87,1.04,1.28,1.64};break;
			default:
				System.out.println("�����alphabet_size̫��");
				break;
			
		}
		
		for(int i = 0 ; i < PAA.size(); i++){
			int sum = 0;
			for(double cut_point : cut_points){
				
				if(cut_point <= PAA.get(i)){
					sum += 1;
				}
			}
			string.add((double) sum);
		}
				
		return string;
	}

	public List<List<Double>> getRealRepresentation(List<List<Double>> timeSeriesData,ParametersObject paramtersObject) {
		
		List<List<Double>> results = new ArrayList<List<Double>>();
		
		for(List<Double> timeSeries : timeSeriesData){
		
			SAXParametersObject saxParametersObject = (SAXParametersObject)paramtersObject;
			if( saxParametersObject.getAlphabet_size() > 20){
				System.out.println("��ǰalphabet_size ���ܳ���20");
				return null;
			}
			saxParametersObject.setN(timeSeries.size());
			saxParametersObject.set_n(timeSeries.size()>8? 8 : timeSeries.size());
			long winsize = MathUtil.floor(saxParametersObject.getN()/saxParametersObject.get_n());
			SymbolicObject symbolicObject = new SymbolicObject(saxParametersObject.get_n());
			
			int[][] allString = new int[timeSeries.size()-saxParametersObject.getN()+1][saxParametersObject.get_n()];
			List<Double> PAA = null;
			double average = MathUtil.mean(timeSeries);
			double std = MathUtil.std(timeSeries);
			
			for(int i = 0 ; i< timeSeries.size(); i ++){
				
				timeSeries.set(i, (timeSeries.get(i)-average)/std);
			}
			
			if(saxParametersObject.get_n()==saxParametersObject.getN()){
				
				PAA = timeSeries;
			}else{
							
				if((saxParametersObject.getN()/saxParametersObject.get_n() - MathUtil.floor(saxParametersObject.getN()/saxParametersObject.get_n()))==0){
					
					PAA = MathUtil.mean(MathUtil.reshape(timeSeries, (int)winsize, saxParametersObject.get_n()),(int)winsize, saxParametersObject.get_n());
				}
			}
			
			List<Double> current_String = mapToString(PAA,saxParametersObject.getAlphabet_size());
			results.add(current_String);
		}
		return results;
	} 
}
