package org.sesamegu;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.log4j.Logger;

public class HttpUtils {
	public static final Logger log = Logger.getLogger(HttpUtils.class);
	private static HttpClient httpClient = null;

	private static final String HOST = "www.qc5qc.com";
	private static final int PORT = 80;

	public static final HttpHost site = new HttpHost(HOST, PORT, "http");

	private static List<Cookie> logoncookies;

	static {
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
		cm.setMaxTotal(50);
		// 多线程的
		httpClient = new DefaultHttpClient(cm);

	}

	public static HttpClient getHttpClient() {
		return httpClient;
	}

	public static String getCookieString() {
		StringBuffer stringBuffer = new StringBuffer();
		for (Cookie item : logoncookies) {
			stringBuffer.append(item.toString());
			stringBuffer.append(";");
		}
		if (stringBuffer.length() > 1) {
			return stringBuffer.substring(0, stringBuffer.length() - 1)
					.toString();
		}
		return stringBuffer.toString();
	}

	public static void setLogoncookies(List<Cookie> logoncookies) {
		HttpUtils.logoncookies = logoncookies;
	}

}
