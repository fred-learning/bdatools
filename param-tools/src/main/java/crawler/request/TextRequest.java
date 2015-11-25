package crawler.request;

public class TextRequest extends Request<String> {
	
	public TextRequest() {
		super("text/html");
	}
	
	@Override
	String process(String content) {
		return content;
	}

}
