package org.sesamegu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

public class AccessReserverPage {

	private final String POST_PAGE_URL = "/xqc/mlpxyy/mCheck.php";
	private final String POST_AREA = "01";
	private final String POST_JX_NAME = "01HJG100";

	public static final Logger log = Logger.getLogger(AccessReserverPage.class);

	public boolean isOk(String personalId) {
		PostParamsDo postParamsDo = new PostParamsDo();
		postParamsDo.setTargetUrl(POST_PAGE_URL);
		postParamsDo.setPersonalId(personalId);
		postParamsDo.setArea(POST_AREA);
		postParamsDo.setJiaoxiaoName(POST_JX_NAME);

		String resultString = doPost(postParamsDo);
		if (resultString == null) {
			log.error("Errors happen!");
			return false;
		}

		if (resultString.indexOf("请从杭州机动车驾驶员模拟培训官方网站上预约！<BR>www.qc5qc.com") != -1) {
			log.info("Still not ok!");
			return false;
		}

		log.info("Happy X! ok now!");

		return true;
	}

	public String doPost(PostParamsDo postParamsDo) {

		// 填入各个表单域的值
		HttpPost httpost = new HttpPost(postParamsDo.getTargetUrl());

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("userSsn", postParamsDo.getPersonalId()));
		nvps.add(new BasicNameValuePair("province", postParamsDo.getArea()));
		nvps.add(new BasicNameValuePair("city", postParamsDo.getJiaoxiaoName()));
		nvps.add(new BasicNameValuePair("token",
				"5f3b43c915777b3e003e4359ad191f4c"));

		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = HttpUtils.getHttpClient().execute(
					HttpUtils.site, httpost);

			HttpEntity entity = response.getEntity();
			// String sb = EntityUtils.toString(entity, "GBK");
			// EntityUtils.consume(entity);

			log.info("----------------------------------------");
			log.info(response.getStatusLine());
			if (entity != null) {
				log.info("Response content length: "
						+ entity.getContentLength());
			}
			// // 显示结果
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "GBK"));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			log.info(sb.toString());
			// reader.close();

			return sb.toString();
		} catch (Exception e) {
			log.error("execute post method", e);
			return null;
		}

	}
}
