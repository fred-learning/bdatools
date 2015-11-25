package lab.paramcfg.backend.storage.others;

public class UnitTest {

	public static void main(String[] args) {
		String url = "http://pc57:18080/history/application_1437031904347_0015/executors/";
		ResCrawler resCrawler = new ResCrawler(url);
		System.out.println("started");
		resCrawler.startResCrawler();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		resCrawler.stopResCrawler();
		System.out.println("stopped");
	}

}
