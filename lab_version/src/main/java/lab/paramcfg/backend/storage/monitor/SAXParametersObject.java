package lab.paramcfg.backend.storage.monitor;

public class SAXParametersObject extends ParametersObject {

	private int N;
	private int n;
	private int alphabet_size;
	
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	public int get_n() {
		return n;
	}
	public void set_n(int n) {
		this.n = n;
	}
	public int getAlphabet_size() {
		return alphabet_size;
	}
	public void setAlphabet_size(int alphabet_size) {
		this.alphabet_size = alphabet_size;
	}
	public SAXParametersObject(int N, int n, int alphabet_size) {
		super();
		this.N = N;
		this.n = n;
		this.alphabet_size = alphabet_size;
	}
	
	
}
