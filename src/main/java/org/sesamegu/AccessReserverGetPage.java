package org.sesamegu;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class AccessReserverGetPage {
	public static final Logger log = Logger
			.getLogger(AccessReserverGetPage.class);

	private static final String RESERVER_PAGE_URL = "/xqc/mlpxyy/mlpxyy.php";
	private static final String FLAG = "<input type=\"hidden\" name=\"token\" id=\"token\" value=\"";

	public String getToken() {
		String result = executeGet(RESERVER_PAGE_URL);
		log.debug("首页文本：\n" + result);
		return pasrseFormHash(result);
	}

	// 从response中取得formhash
	String pasrseFormHash(String response) {

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
	public static String executeGet(String url) {
		String result;
		HttpGet httpget = new HttpGet(RESERVER_PAGE_URL);
		try {
			HttpResponse rsp = HttpUtils.getHttpClient().execute(
					HttpUtils.site, httpget);
			HttpEntity entity = rsp.getEntity();
			result = EntityUtils.toString(entity, "GBK");
			// result = Util.ConverterStringCode(result, EntityUtils
			// .getContentCharSet(entity), "GBK");

			List<Cookie> cookies = ((DefaultHttpClient) HttpUtils
					.getHttpClient()).getCookieStore().getCookies();

			HttpUtils.setLogoncookies(cookies);

			// for (Cookie item : cookies) {
			// log.info("cookie = " + item.toString());
			// }

		} catch (Exception e) {
			httpget.abort();
			log.error("executeGet ", e);
			return "";
		}
		return result;
	}

	public static void main(String[] args) {
		AccessReserverGetPage get = new AccessReserverGetPage();

		System.out.println(get.getToken());

	}
}
