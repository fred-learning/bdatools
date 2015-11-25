package recommend.basic;


public class Nodem {
	private String id;
	private String label;
	
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Nodem)) return false;
		return ((Nodem) obj).getId().equals(id);
	}
	
}
