package lab.paramcfg.backend.storage.monitor;

import java.util.List;

public class SymbolicObject {

	public int[][] symbolic_data;
	public List<Integer> pointers;
	
	public SymbolicObject(int n){
		
		symbolic_data = new int[1][n];
	}
}
