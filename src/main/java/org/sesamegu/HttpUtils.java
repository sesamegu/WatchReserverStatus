package org.sesamegu;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.log4j.Logger;

public class HttpUtils {
	public static final Logger log = Logger.getLogger(HttpUtils.class);
	private static HttpClient httpClient = null;

	private static final String HOST = "www.qc5qc.com";
	private static final int PORT = 80;

	public static final HttpHost site = new HttpHost(HOST, PORT, "http");

	static {
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
		cm.setMaxTotal(50);

		// 多线程的
		httpClient = new DefaultHttpClient(cm);
		// 初始化httpclient
		// httpClient.getHostConfiguration().setHost(HOST, PORT);
		// httpClient.getParams().setParameter(
		// CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "GBK");
		// httpClient.getParams().setParameter(
		// CoreProtocolPNames.HTTP_CONTENT_CHARSET, "GBK");
		// TODO:
		// httpClient.getParams().setCookiePolicy(
		// CookiePolicy.BROWSER_COMPATIBILITY);

	}

	public static HttpClient getHttpClient() {
		return httpClient;
	}

}
