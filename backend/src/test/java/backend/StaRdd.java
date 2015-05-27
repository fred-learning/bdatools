package backend;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class StaRdd {

	public enum BSize
	 {
	     B,KB,MB,GB; 
	 }
	
	@Test
	public void test() throws Exception {
		System.out.println("start");
		TreeMap<Integer, Float> rddmap = new TreeMap<Integer, Float>();

		BufferedReader strCon = new BufferedReader(new FileReader(
				"D:/bmlog/km/e2_km3_30_100"));
		String context;
		while ((context = strCon.readLine()) != null) {
			if (!context.equals("")) {
				String regex = "^\\d{2}/\\d{2}/\\d{2}";
				Pattern pt = Pattern.compile(regex);
				Matcher matcher = pt.matcher(context);
				if (matcher.find()) {
					try {
						String[] part = context.split(" ");
						String timestamp = context.substring(0, 17);
						// String logtype = part[2];
						String component = part[3].substring(0,
								part[3].length() - 1);
						
						//below is new add part
						if(component.equals("storage.MemoryStore")){
							if (part.length==18&&part[5].startsWith("rdd")){
								System.out.println(part[5]+"--"+part[13]+"--"+part
										[14]);
								int key = Integer.valueOf(part[5].charAt(4))-48;
								float value = Float.valueOf(part[13]);
								String unit = part[14].substring(0, part[14].length()-1);
								switch (BSize.valueOf(unit)) {
								case B:
									value/=1024;
									break;
								case MB:
									value*=1024;
									break;
								case GB:
									value*=1024*1024;
									break;
								default:
									break;
								}
								if (rddmap.containsKey(key)) {
									float tmpvalue = rddmap.get(key);
									rddmap.put(key, value+tmpvalue);
								}else {
									rddmap.put(key, value);
								}
							}
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			}
		}
		rddmap.size();
	}

}
