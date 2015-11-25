package lab.paramcfg.backend.storage.journal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMatch {
	private static HashMap<String, HashSet<String>> map = LogMap.logmap;
	
	private static void linematch(String context) {
		if (context.equals(""))
			return;
		String regex = "^\\d{2}/\\d{2}/\\d{2}";
		Pattern pt = Pattern.compile(regex);
		Matcher matcher = pt.matcher(context);
		if (matcher.find()) {
			try {
				String[] part = context.split(" ");
				String timestamp = context.substring(0, 17);
				// String logtype = part[2];
				String component = part[3].substring(0, part[3].length() - 1);
				String logString = context.substring(part[0].length()+part[1].length()+part[2].length()+part[3].length()+4, context.length());
//				System.out.println(logString);
				if (map.containsKey(component)) {
					HashSet<String> tmpset = map.get(component);
					boolean flag = false;
					int count=0;
					for (String tmplog : tmpset) {
//						System.out.println(tmplog);
						count++;
						regex = tmplog;
						pt = Pattern.compile(regex);
						matcher = pt.matcher(logString);
						if (matcher.find()) {
							System.out.println(component+count);
							flag = true;
							break;
						}
					}
					if (!flag) {
//						System.out.println("not match log---"+context);
					}
				}else {
//					System.out.println("not find key---"+context);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}
	
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("D:/examplelog");
		BufferedReader br = new BufferedReader(fr);
		String line;
		while ((line = br.readLine()) != null) {
			linematch(line);
		}
		br.close();
	}
}
