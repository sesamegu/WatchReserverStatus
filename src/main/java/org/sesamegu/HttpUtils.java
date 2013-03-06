package org.sesamegu;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class HttpUtils {
	public static final Logger log = Logger.getLogger(HttpUtils.class);
	// private static HttpClient httpClient = null;

	private static final String HOST = "www.qc5qc.com";
	private static final int PORT = 80;

	public static final HttpHost site = new HttpHost(HOST, PORT, "http");

	private static HttpClient httpClient;

	public static HttpClient getHttpClient(boolean isNeedNew) {
		if (isNeedNew) {
			httpClient = new DefaultHttpClient();
		}
		return httpClient;

	}

}
