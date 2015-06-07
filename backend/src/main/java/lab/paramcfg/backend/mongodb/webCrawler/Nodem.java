package lab.paramcfg.backend.mongodb.webCrawler;

public class Nodem{
	private String id = null;
	private String label = null;
	
	public Nodem(String id, String label){
		this.id = id;
		this.label = label;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}