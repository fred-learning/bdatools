package crawler.env;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class YarnEnvParser {
    private String xml;

    public YarnEnvParser(String xml) {
        this.xml = xml;
    }

    public Env getEnv() {
        Env env = new Env();
        Document doc = Jsoup.parse(xml);
        for (Element elem : doc.getElementsByTag("clusterMetrics").first().children()) {
            String key = elem.tagName();
            String val = elem.text();
            env.put(key, val);
        }
        return env;
    }
}
