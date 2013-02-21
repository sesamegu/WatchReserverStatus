package org.sesamegu;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 系统主入口
 */
public class App {

	public static final Logger log = Logger.getLogger(App.class);

	public static void main(String[] args) throws InterruptedException {
		AccessReserverPostPage abc = new AccessReserverPostPage();
		while (!abc.isOk(SystemParams.id)) {
			TimeUnit.MINUTES.sleep(5);
		}
		log.info("系统正常结束！");
	}
}
