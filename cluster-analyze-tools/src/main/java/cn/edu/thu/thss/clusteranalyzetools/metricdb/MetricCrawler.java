package cn.edu.thu.thss.clusteranalyzetools.metricdb;

import cn.edu.thu.thss.clusteranalyzetools.common.Config;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Set;

public class MetricCrawler {
    private static Logger logger = Logger.getLogger(MetricCrawler.class);
    private static Config config = Config.getInstance();

    public static ClusterMetric getClusterMetric() {
        String xml = getClusterXML();
        if (xml == null) return null;
        return parseClusterMetricXML(xml);
    }

    private static String getClusterXML() {
        String response = null;

        String url = config.getClusterDataRequestPath();
        logger.info("Getting Cluster Metric from " + config.getClusterDataRequestPath());
        try {
            HttpResponse<String> resp =
                    Unirest.get(url).header("accept", "application/xml").asString();
            if (resp.getStatus() != 200) {
                logger.error(String.format("Getting data from %s failed. Response status %d.",
                        url, resp.getStatus()));
            } else {
                response = resp.getBody();
            }
        } catch (UnirestException e) {
            logger.error("Error when getting data from " + url, e);
        }
        return response;
    }

    private static ClusterMetric parseClusterMetricXML(String content) {
        ClusterMetric ret = new ClusterMetric();

        Document doc = Jsoup.parse(content);
        Elements elements = doc.getElementsByTag("host");
        for (Element element : elements) {
            Pair<String, HostMetric> pair = parseHostMetricXML(element);
            if (pair != null) ret.addHostMetric(pair.getLeft(), pair.getRight());
        }
        return ret;
    }

    private static Pair<String, HostMetric> parseHostMetricXML(Element element) {
        Set<String> allowHost = config.getClusterHosts();
        Attributes attributes = element.attributes();
        String host = attributes.get("name");
        Long reportTime = Long.parseLong(attributes.get("REPORTED"));

        if (!allowHost.contains(host)) {
            return null;
        } else {
            HostMetric hostMetric = new HostMetric(reportTime);
            Set<String> allowMetrics = config.getMetricNames();
            for (Element metricElem : element.getElementsByTag("METRIC")) {
                Attributes metricAttributes = metricElem.attributes();
                String metricName = metricAttributes.get("name");
                if (!allowMetrics.contains(metricName)) continue;
                Float val = Float.parseFloat(metricAttributes.get("val"));
                hostMetric.addMetric(metricName, val);
            }
            return Pair.of(host, hostMetric);
        }
    }

}
