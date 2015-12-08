package cn.edu.thu.thss.clusteranalyzetools.common;

import java.util.HashSet;
import java.util.Set;

public class CollectionUtil {

    public static Set<String> strArrayToSet(String[] array) {
        Set<String> ret = new HashSet<String>();
        for (String elem : array) ret.add(elem);
        return ret;
    }
}
