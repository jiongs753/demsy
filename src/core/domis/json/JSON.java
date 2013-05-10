package domis.json;

import java.io.Writer;

import domis.json.nutz.JSONNutzImpl;

/**
 * JSON工具类
 * 
 * @author yongshan.ji
 * 
 */
public class JSON {
	/**
	 * JSON的实现
	 */
	public static final JSONApi json = new JSONNutzImpl();

	public static String toJson(Object obj) {
		return json.toJson(obj);
	}

	public static void toJson(Writer writer, Object obj) {
		json.toJson(writer, obj);
	}

	public static interface JSONApi {

		public String toJson(Object obj);

		public void toJson(Writer writer, Object obj);
	}
}
