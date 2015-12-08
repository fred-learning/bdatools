package cn.edu.thu.thss.clusteranalyzetools.metricdb;

import java.util.HashMap;
import java.util.Map;

public class ClusterMetric {
    private Map<String, HostMetric> hostMetrics;

    public ClusterMetric() {
        hostMetrics = new HashMap<String, HostMetric>();
    }

    public void addHostMetric(String hostname, HostMetric metric) {
        hostMetrics.put(hostname, metric);
    }
}
