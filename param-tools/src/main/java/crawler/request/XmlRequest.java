package crawler.request;

public class XmlRequest extends Request<String> {

    public XmlRequest() {
        super("application/xml");
    }

    @Override
    String process(String content) {
        return content;
    }

}
