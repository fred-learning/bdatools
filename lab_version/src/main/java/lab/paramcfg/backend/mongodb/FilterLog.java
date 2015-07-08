package lab.paramcfg.backend.mongodb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lab.paramcfg.backend.common.LogMap;

public class FilterLog {
	
	private static String parseline(String log) {
		String ret = "";
		String context = log.substring(log.indexOf("(") + 1, log.length() - 1);
//		System.out.println(context);
		
		String patternString = "\"(.+?)\"";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(context);
		while (matcher.find()) {
//			System.out.println("found: " + matcher.group(1));
			ret+=matcher.group(1)+"*";
		}
//		System.out.println("!!!:"+ret);
		ret = ret.replaceAll("\\$\\w+", "*");
		ret = ret.replaceAll("\\$\\{.+\\}", "*");
		ret = ret.replaceAll("%[\\w.\\d]+", "*");
//		System.out.println("###:"+ret);
		return ret;
	}

	private static void filter(String path) throws Exception {
		FileWriter writer = new FileWriter("D:/loglist", true);
        BufferedWriter bw = new BufferedWriter(writer);
        
		String[] filearr = path.split("\\\\");
		String part1 = filearr[filearr.length - 2];
		String[] part2 = filearr[filearr.length - 1].split("\\.");

		boolean foundflag = false;
		String content = part1 + "." + part2[0];
		String log = null;

		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("logInfo") | line.startsWith("logDebug")
					| line.startsWith("logTrace")
					| line.startsWith("logWarning")
					| line.startsWith("logError")) {
				foundflag = true;
				log = null;
				log = line;
				if (line.endsWith(")")) {
					// System.out.println(content+": "+log);
					log = parseline(log);
					if (!log.equals("")) {
						bw.write(content+":"+log);
						bw.newLine();
						bw.flush();
					}
					foundflag = false;
				}
			} else {
				if (foundflag) {
					if (line.endsWith(")")) {
						log += line;
						// System.out.println(content+": "+log);
						log = parseline(log);
						if (!log.equals("")) {
							bw.write(content+":"+log);
							bw.newLine();
							bw.flush();
						}
						foundflag = false;
					} else {
						log += line;
					}
				}
			}
		}
		br.close();
		fr.close();
        bw.close();
        writer.close();
	}

	public static void main(String[] args) throws Exception {		
		File file = new File("D:/sparkforfilterlogline");
		if (file.exists()) {
			LinkedList<File> list = new LinkedList<File>();
			File[] files = file.listFiles();
			for (File file2 : files) {
				if (file2.isDirectory()) {
					list.add(file2);
				} else {
					filter(file2.getAbsolutePath());
				}
			}
			File temp_file;
			while (!list.isEmpty()) {
				temp_file = list.removeFirst();
				files = temp_file.listFiles();
				for (File file2 : files) {
					if (file2.isDirectory()) {
						list.add(file2);
					} else {
						filter(file2.getAbsolutePath());
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		System.out.println("finished!");
	}
}
