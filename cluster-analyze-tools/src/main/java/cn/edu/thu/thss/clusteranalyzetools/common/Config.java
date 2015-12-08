package cn.edu.thu.thss.clusteranalyzetools.common;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Config {
    private static Logger logger = Logger.getLogger(Config.class);
    private static Config config;
    private Properties properties;

    public synchronized static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private Config() {
        try {
            InputStream stream = this.getClass().getResourceAsStream("/settings.properties");
            properties = new Properties();
            properties.load(stream);
        } catch (IOException e) {
            logger.fatal("Error when reading default settings.properties:\n", e);
            System.exit(-1);
        }
    }

    public void loadSettings(String path) {
        try {
            FileInputStream fs = new FileInputStream(path);
            properties = new Properties();
            properties.load(fs);
        } catch (IOException e) {
            logger.fatal("Error when loading " + path + "\n", e);
            System.exit(-1);
        }
    }

    public String getClusterDataRequestPath() {
        assert properties.getProperty("ganglia_rest_path") != null;
        assert properties.getProperty("cluster_name") != null;
        return String.format("%s/%s", properties.getProperty("ganglia_rest_path"),
                properties.getProperty("cluster_name"));
    }

    public Set<String> getClusterHosts() {
        assert properties.getProperty("ganglia_monitor_hosts") != null;
        String[] array = properties.getProperty("ganglia_monitor_hosts").split(",");
        return CollectionUtil.strArrayToSet(array);
    }

    public Set<String> getMetricNames() {
        Set<String> ret = new HashSet<String>();
        ret.addAll(getCpuMetricNames());
        ret.addAll(getNetworkMetricNames());
        ret.addAll(getMemoryMetricNames());
        ret.addAll(getDiskMetricNames());
        return ret;
    }

    public Set<String> getCpuMetricNames() {
        assert properties.getProperty("ganglia_cpu_metrics") != null;
        return CollectionUtil.strArrayToSet(
                properties.getProperty("ganglia_cpu_metrics").split(","));
    }

    public Set<String> getNetworkMetricNames() {
        assert properties.getProperty("ganglia_network_metrics") != null;
        return CollectionUtil.strArrayToSet(
                properties.getProperty("ganglia_network_metrics").split(","));
    }

    public Set<String> getMemoryMetricNames() {
        assert properties.getProperty("ganglia_memory_metrics") != null;
        return CollectionUtil.strArrayToSet(
                properties.getProperty("ganglia_memory_metrics").split(","));
    }

    public Set<String> getDiskMetricNames() {
        assert properties.getProperty("ganglia_disk_metrics") != null;
        return CollectionUtil.strArrayToSet(
                properties.getProperty("ganglia_disk_metrics").split(","));
    }

    public String getMongoIP() {
        assert properties.getProperty("mongodb_ip") != null;
        return properties.getProperty("mongodb_ip");
    }

    public int getMongoPort() {
        assert properties.getProperty("mongodb_port") != null;
        return Integer.parseInt(properties.getProperty("mongodb_port"));
    }

    public String getMongoDBName() {
        assert properties.getProperty("mongodb_metric_db") != null;
        return properties.getProperty("mongodb_metric_db");
    }

    public String getMongoCollectionName() {
        assert properties.getProperty("cluster_name") != null;
        return "col_" + properties.getProperty("cluster_name");
    }

    public Integer getMongoSyncInterval() {
        assert properties.getProperty("mongodb_metric_sync_interval_sec") != null;
        return Integer.parseInt(properties.getProperty("mongodb_metric_sync_interval_sec"));
    }
}
