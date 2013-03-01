package org.sesamegu;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.sesamegu.tools.mail.MailMail;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 系统主入口
 */
public class App {

	public static final Logger log = Logger.getLogger(App.class);

	private static ApplicationContext context;

	public static void main(String[] args) throws InterruptedException {
		// 启动Spring
		context = new ClassPathXmlApplicationContext("Spring-Mail.xml");

		// 爬虫
		AccessReserverPostPage abc = new AccessReserverPostPage();
		while (!abc.isOk(SystemParams.id)) {
			// TimeUnit.SECONDS.sleep(5);
			TimeUnit.MINUTES.sleep(5);
		}
		// 成功后发邮件
		MailMail mm = (MailMail) context.getBean("mailMail");
		mm.sendMail("haiquan81@163.com", "haiquan.guhq@alibaba-inc.com",
				"驾校能够报名了！", "驾校能够报名了！");

		log.info("系统正常结束！");

	}
}
