package lab.paramcfg.backend.storage.monitor;

import java.util.ArrayList;
import java.util.List;
/**
 * Matlab��Ӧ�ĺ���
 * @author Apache
 *
 */
public class MathUtil {

	/**
	 * ����ȡ��
	 * @param number
	 * @return
	 */
	 public static long floor(double number){
		
		return Math.round(number);
	};
	
	/**
	 * ��ƽ��ֵ
	 * @param values
	 * @return
	 */
	public static double mean(List<Double> values){
		
		double total = 0, average = 0;
		
		for(Double value : values){
			
			total += value;
		}
		
		average = total / values.size();
		
		return average;
	}
	/**
	 * ��ά������ƽ��ֵ
	 * @param values
	 * @param N
	 * @param n
	 * @return
	 */
	public static List<Double> mean(double[][] values,int N,int n){
		
		List<Double> lists = new ArrayList<Double>();
		
		for(int i = 0 ; i < n ; i++ ){
			
			List<Double> columns = new ArrayList<Double>();
			
			for(int j = 0 ; j< N; j ++){
				
				columns.add(values[j][i]);
			}
			lists.add(mean(columns));
		}
		
		return lists;
	}
	
	/***
	 * ���׼��
	 * @param values
	 * @return
	 */
	public static double std(List<Double> values){
		
		double average = 0, std = 0, sum = 0;
		average = mean(values);
		
		for(Double value : values){
			
			sum += Math.pow((value - average),2);
		}
		sum /=(values.size()-1);
		std = Math.sqrt(sum);
		
		return std;
	}
	
	/**
	 * ���½�һά������� Nxn ά����
	 * @param values
	 * @param N
	 * @param n
	 * @return
	 */
	public static double[][] reshape(List<Double> values,int N, int n){
		
		double[][] matrixs = new double[N][n];
		
		for(int i = 0 ; i < n ; i++ ){
			for(int j = 0 ; j< N; j ++){
				
				matrixs[j][i] = values.get(j + i * N);
			}
		}
		
		return matrixs;
		
	}
}


