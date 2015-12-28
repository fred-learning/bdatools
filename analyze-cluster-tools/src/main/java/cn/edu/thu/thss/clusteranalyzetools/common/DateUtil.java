package cn.edu.thu.thss.clusteranalyzetools.common;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Long getNowTime() {
        return new Date().getTime();
    }

}
