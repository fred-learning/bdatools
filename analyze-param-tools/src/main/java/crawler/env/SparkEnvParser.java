package crawler.env;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SparkEnvParser {
    private String html;

    public SparkEnvParser(String html) {
        this.html = html;
    }

    public Env getEnv() {
        Env env = new Env();
        Document doc = Jsoup.parse(html);
        for (Element tbody : doc.getElementsByTag("tbody")) {
            for (Element tr : tbody.getElementsByTag("tr")) {
                Elements elems = tr.getElementsByTag("td");
                String key = elems.first().text();
                String val = elems.get(1).text();
                env.put(key, val);
            }
        }
        return env;
    }
}
