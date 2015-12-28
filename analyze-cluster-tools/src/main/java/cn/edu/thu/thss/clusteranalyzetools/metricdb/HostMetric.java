package cn.edu.thu.thss.clusteranalyzetools.metricdb;

import java.util.HashMap;
import java.util.Map;

public class HostMetric {
    private Long reportTime;
    private Map<String, Float> metrics;

    public HostMetric(Long reportTime) {
        metrics = new HashMap<String, Float>();
        this.reportTime = reportTime;
    }

    public void addMetric(String key, Float val) {
        metrics.put(key, val);
    }

    public Float getMetric(String key) {
        assert metrics.containsKey(key);
        return metrics.get(key);
    }

    public Long getReportTime() {
        return reportTime;
    }
}
