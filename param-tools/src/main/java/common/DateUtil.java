package common;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;

public class DateUtil {
    private static Logger logger = Logger.getLogger(DateUtil.class);

    public static Long stringToTime(String s) {
        Long ret = 0L;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
            ret = format.parse(s).getTime();
        } catch (Exception e) {
            logger.fatal("Invalid time string: " + s);
            System.exit(-1);
        }
        return ret;
    }

}
