package org.sesamegu;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class AccessReserverGetPage {
	public static final Logger log = Logger
			.getLogger(AccessReserverGetPage.class);

	private static final String RESERVER_PAGE_URL = "/xqc/mlpxyy/mlpxyy.php";
	private static final String FLAG = "<input type=\"hidden\" name=\"token\" id=\"token\" value=\"";

	public String getToken() {
		CountDownLatch cdl = new CountDownLatch(1);
		CurlThread def = new CurlThread(cdl);
		Thread abc = new Thread(def);
		abc.setDaemon(true);
		abc.start();
		try {
			boolean isEnd = cdl.await(10l, TimeUnit.SECONDS);
			if (!isEnd) {
				log.info("One Thread die!");
				abc.interrupt();
			}
		} catch (InterruptedException e) {
		}
		return def.getToken();
	}

	// 从response中取得formhash
	public static String pasrseFormHash(String response) {

		String[] split = response.split("\n");
		for (String item : split) {
			int position = item.indexOf(FLAG);
			if (position != -1) {
				int startPosition = position + FLAG.length();
				int endPosition = item.lastIndexOf("\"");

				return item.substring(startPosition, endPosition);
			}
		}

		return "";
	}

	/**
	 * 取得对应URL的文本
	 */
	public static String executeGet() {
		String result;
		HttpGet httpget = new HttpGet(RESERVER_PAGE_URL);
		try {
			HttpResponse rsp = HttpUtils.getHttpClient(true).execute(
					HttpUtils.site, httpget);
			HttpEntity entity = rsp.getEntity();
			result = EntityUtils.toString(entity, "GBK");

		} catch (Exception e) {
			httpget.abort();
			log.error("executeGet ", e);
			return "";
		}
		return result;
	}

	class CurlThread implements Runnable {

		private CountDownLatch cdl;
		private String token;

		public CurlThread(CountDownLatch cdl) {
			this.cdl = cdl;
		}

		public void run() {
			String result = executeGet();
			log.debug("首页文本：\n" + result);
			token = pasrseFormHash(result);
			cdl.countDown();
		}

		public String getToken() {
			return token;
		}

	}

	public static void main(String[] args) {
		AccessReserverGetPage get = new AccessReserverGetPage();

		System.out.println(get.getToken());

	}
}
