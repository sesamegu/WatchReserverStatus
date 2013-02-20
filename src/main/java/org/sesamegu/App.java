package org.sesamegu;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws InterruptedException {

		AccessReserverPage abc = new AccessReserverPage();
		while (!abc.isOk("tttt")) {
			// TimeUnit.SECONDS.sleep(3);
			TimeUnit.MINUTES.sleep(30);
		}
		System.out.println("*************:))))))))))))))))))))!");
	}
}
