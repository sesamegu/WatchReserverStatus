package org.sesamegu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 * 小程序的整体流程： 1）先访问get页面，取得token和cookie，其中cookie由http client自动完成填充;
 * 2）准备表单数据，并提交；\n 3）检查表单返回的结果；
 */
public class AccessReserverPostPage {

	private final String POST_PAGE_URL = "/xqc/mlpxyy/mCheck.php";

	private final String POST_AREA = "01";
	private final String POST_JX_NAME = "01HJG100";

	public static final Logger log = Logger
			.getLogger(AccessReserverPostPage.class);

	public boolean isOk(String personalId) {
		PostParamsDo postParamsDo = new PostParamsDo();
		postParamsDo.setTargetUrl(POST_PAGE_URL);
		postParamsDo.setPersonalId(personalId);
		postParamsDo.setArea(POST_AREA);
		postParamsDo.setJiaoxiaoName(POST_JX_NAME);

		AccessReserverGetPage get = new AccessReserverGetPage();

		String token = get.getToken();
		if (token == null || token.equals("")) {
			log.error("Errors happen!");
			return false;
		}

		postParamsDo.setToken(token);

		try {
			TimeUnit.SECONDS.sleep(3);
			log.debug("休息3秒");
		} catch (InterruptedException e) {
		}

		String resultString = doPost(postParamsDo);
		if (resultString == null) {
			log.error("Errors happen!");
			return false;
		}

		if (resultString
				.indexOf("<script language='javascript'>alert('该学员已完成模拟培训。');history.back(-1);</script>") != -1) {
			log.info("不好意思，当前还不能报名！");
			return false;
		}

		log.info("执行成功，可以报名了！");

		return true;
	}

	public String doPost(PostParamsDo postParamsDo) {

		// 填入各个表单域的值
		HttpPost httpost = new HttpPost(postParamsDo.getTargetUrl());

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("userSsn", postParamsDo.getPersonalId()));
		nvps.add(new BasicNameValuePair("province", postParamsDo.getArea()));
		nvps.add(new BasicNameValuePair("city", postParamsDo.getJiaoxiaoName()));
		nvps.add(new BasicNameValuePair("token", postParamsDo.getToken()));

		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "GBK"));
			HttpResponse response = HttpUtils.getHttpClient(false).execute(
					HttpUtils.site, httpost);

			HttpEntity entity = response.getEntity();
			// String sb = EntityUtils.toString(entity, "GBK");
			// EntityUtils.consume(entity);

			log.debug("----------------------------------------");
			log.debug(response.getStatusLine());
			if (entity != null) {
				log.debug("Response content length: "
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
