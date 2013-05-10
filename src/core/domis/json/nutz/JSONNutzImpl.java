package domis.json.nutz;

import java.io.Writer;

import org.nutz.json.Json;

import domis.json.JSON.JSONApi;

public class JSONNutzImpl implements JSONApi {

	@Override
	public String toJson(Object obj) {
		return Json.toJson(obj);
	}

	@Override
	public void toJson(Writer writer, Object obj) {
		Json.toJson(writer, obj);
	}

}
