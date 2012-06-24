import java.io.IOException;
import java.util.Enumeration;

import com.kmetop.demsy.lang.Img;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// IPSeeker ip = new IPSeeker("QQWry.Dat",
		// "D:\\Research\\DEMSY.V2.x\\src\\web\\WEB-INF\\lib");
		// // 获得地区
		// System.out.println(ip.getIPLocation("58.62.226.211").getCountry());
		// // 获得类型
		// System.out.println(ip.getIPLocation("58.62.226.211").getArea());

		// System.out.print((int) '0' + "," + (int) '9');

		printClassPath();
	}

	public static void testZoomImage() {
		String srcImg = "C:\\Users\\yongshan.ji\\Desktop\\temp\\IMG_9401.jpg";
		String targetImg = "C:\\Users\\yongshan.ji\\Desktop\\temp\\IMG_9401_small.jpg";
		Img.zoomImage(srcImg, targetImg, 6000, 6000, true);
	}

	public static void printClassPath() {
		Enumeration keys = System.getProperties().keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			Object value = System.getProperty(key);
			System.out.println(key + " = " + value);
		}
	}
}
