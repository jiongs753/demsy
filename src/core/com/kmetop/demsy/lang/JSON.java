package com.kmetop.demsy.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.nutz.json.Json;
import org.nutz.json.JsonException;
import org.nutz.json.JsonFormat;
import org.nutz.json.ToJson;
import org.nutz.lang.FailToGetValueException;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.entity.IBizEntity;

public class JSON {
	private static String NL = "\n";

	private HashMap<Object, Object> memo;

	private Writer writer;

	/**
	 * 
	 * @param writer
	 * @param format
	 * @throws NullPointerException
	 *             if writer or format is null
	 */
	public JSON(Writer writer, JsonFormat format) {
		this.format = format;
		this.writer = writer;
		// TODO make a new faster collection
		// implementation
		memo = new HashMap<Object, Object>();
	}

	private JsonFormat format;

	private static boolean isCompact(JSON render) {
		return render.format.isCompact();
	}

	private static final Pattern p = Pattern.compile("^[a-z_A-Z$]+[a-zA-Z_0-9$]*$");

	private void appendName(String name) throws IOException {
		if (!p.matcher(name).find())
			string2Json(name);
		else
			writer.append(name);
	}

	private void appendPairBegin() throws IOException {
		if (!isCompact(this))
			writer.append(NL).append(Strings.dup(format.getIndentBy(), format.getIndent()));
	}

	private void appendPairSep() throws IOException {
		writer.append(" : ");
	}

	private void appendPair(String name, Object value) throws IOException {
		appendPairBegin();
		appendName(name);
		appendPairSep();
		renderValue(value);
	}

	public void renderValue(Object obj) throws IOException {
		if (null == obj)
			writer.append("null");
		else {
			Mirror mr = Mirror.me(obj.getClass());
			if (mr.isEnum()) {
				string2Json(((Enum) obj).name());
			} else if (mr.isNumber() || mr.isBoolean() || mr.isChar()) {
				writer.append(obj.toString());
			} else if (mr.isStringLike()) {
				string2Json(obj.toString());
			} else if (Cls.isDate(obj.getClass())) {
				string2Json(Dates.formatDateTime((Date) obj));
			} else if (obj instanceof IBizEntity) {
				IBizEntity en = (IBizEntity) obj;
				writer.append("{entityGuid : ");
				string2Json(en.getEntityGuid());
				writer.append("}");
			} else {
				pojo2Json(obj);
			}
		}
	}

	private boolean isIgnore(String name, Object value) {
		if (null == value && format.isIgnoreNull())
			return true;
		return format.ignore(name);
	}

	private void appendPairEnd() throws IOException {
		writer.append(',');
	}

	private void appendBraceBegin() throws IOException {
		writer.append("{");
	}

	private void appendBraceEnd() throws IOException {
		if (!isCompact(this))
			writer.append(NL).append(Strings.dup(format.getIndentBy(), format.getIndent()));
		writer.append("}");
	}

	static class Pair {

		public Pair(String name, Object value) {
			this.name = name;
			this.value = value;
		}

		String name;

		Object value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void map2Json(Map map) throws IOException {
		if (null == map)
			return;
		appendBraceBegin();
		increaseFormatIndent();
		ArrayList<Pair> list = new ArrayList<Pair>(map.size());
		Set<Entry<?, ?>> entrySet = map.entrySet();
		for (Entry entry : entrySet) {
			String name = null == entry.getKey() ? "null" : entry.getKey().toString();
			Object value = entry.getValue();
			if (!this.isIgnore(name, value))
				list.add(new Pair(name, value));
		}
		for (Iterator<Pair> it = list.iterator(); it.hasNext();) {
			Pair p = it.next();
			this.appendPair(p.name, p.value);
			if (it.hasNext())
				this.appendPairEnd();
		}

		decreaseFormatIndent();
		appendBraceEnd();
	}

	private void pojo2Json(Object obj) throws IOException {
		if (null == obj)
			return;
		Class<? extends Object> type = obj.getClass();
		ToJson tj = type.getAnnotation(ToJson.class);
		String myMethodName = Strings.sNull(null == tj ? null : tj.value(), "toJson");
		/*
		 * toJson()
		 */
		try {
			Method myMethod = type.getMethod(myMethodName);
			if (!myMethod.isAccessible())
				myMethod.setAccessible(true);
			Object re = myMethod.invoke(obj);
			writer.append(String.valueOf(re));
			return;
		}
		/*
		 * toJson(JsonFormat fmt)
		 */
		catch (Exception e1) {
			try {
				Method myMethod = type.getMethod(myMethodName, JsonFormat.class);
				if (!myMethod.isAccessible())
					myMethod.setAccessible(true);
				Object re = myMethod.invoke(obj, format);
				writer.append(String.valueOf(re));
				return;
			} catch (Exception e) {
			}
		}
		/*
		 * Default
		 */
		Mirror<?> me = Mirror.me(type);
		Field[] fields = me.getFields();
		appendBraceBegin();
		increaseFormatIndent();
		ArrayList<Pair> list = new ArrayList();
		for (Field f : fields) {
			Class ftype = f.getType();
			if (Map.class.isAssignableFrom(ftype) || Collection.class.isAssignableFrom(ftype) || ftype.isArray()) {
				continue;
			}
			String name = f.getName();
			try {
				Object value = me.getValue(obj, name);
				if (!this.isIgnore(name, value))
					list.add(new Pair(name, value));
			} catch (FailToGetValueException e) {
			}
		}
		for (Iterator<Pair> it = list.iterator(); it.hasNext();) {
			Pair p = it.next();
			this.appendPair(p.name, p.value);
			if (it.hasNext())
				this.appendPairEnd();
		}
		decreaseFormatIndent();
		appendBraceEnd();
	}

	private void decreaseFormatIndent() {
		if (!isCompact(this))
			format.decreaseIndent();
	}

	private void increaseFormatIndent() {
		if (!isCompact(this))
			format.increaseIndent();
	}

	private void string2Json(String s) throws IOException {
		if (null == s)
			writer.append("null");
		else {
			char[] cs = s.toCharArray();
			writer.append(format.getSeparator());
			for (char c : cs) {
				switch (c) {
				case '"':
					writer.append("\\\"");
					break;
				case '\n':
					writer.append("\\n");
					break;
				case '\t':
					writer.append("\\t");
					break;
				case '\r':
					writer.append("\\r");
					break;
				case '\\':
					writer.append("\\\\");
					break;
				default:
					if (c >= 256 && format.isAutoUnicode())
						writer.append("\\u").append(Integer.toHexString(c).toUpperCase());
					else
						writer.append(c);
				}
			}
			writer.append(format.getSeparator());
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public void render(Object obj) throws IOException {
		if (null == obj) {
			writer.write("null");
		} else if (obj instanceof Class) {
			string2Json(((Class<?>) obj).getName());
		} else if (obj instanceof Mirror) {
			string2Json(((Mirror<?>) obj).getType().getName());
		} else {
			Mirror mr = Mirror.me(obj.getClass());
			if (mr.isEnum()) {
				string2Json(((Enum) obj).name());
			} else if (mr.isNumber() || mr.isBoolean() || mr.isChar()) {
				writer.append(obj.toString());
			} else if (mr.isStringLike()) {
				string2Json(obj.toString());
			} else if (mr.isDateTimeLike()) {
				string2Json(format.getCastors().castToString(obj));
			} else if (memo.containsKey(obj)) {
				writer.append("null");
			} else {
				memo.put(obj, null);
				if (obj instanceof Map)
					map2Json((Map) obj);
				else if (obj instanceof Collection)
					coll2Json((Collection) obj);
				else if (obj.getClass().isArray())
					array2Json(obj);
				else {
					pojo2Json(obj);
				}
				memo.remove(obj);
			}
		}
	}

	private void array2Json(Object obj) throws IOException {
		writer.append('[');
		int len = Array.getLength(obj) - 1;
		if (len > -1) {
			int i;
			for (i = 0; i < len; i++) {
				// render(Array.get(obj, i));
				writer.append("" + Array.get(obj, i));
				writer.append(',').append(' ');
			}
			render(Array.get(obj, i));
		}
		writer.append(']');
	}

	private void coll2Json(Collection<?> obj) throws IOException {
		writer.append('[');
		for (Iterator<?> it = obj.iterator(); it.hasNext();) {
			// render(it.next());
			writer.append("" + it.next());
			if (it.hasNext())
				writer.append(',').append(' ');
		}
		writer.append(']');
	}

	public static String toJson(Object obj) {
		try {
			StringBuilder sb = new StringBuilder();
			Writer writer = Lang.opw(sb);
	
			new JSON(writer, JsonFormat.nice()).render(obj);
	
			writer.flush();
	
			return sb.toString();
		} catch (IOException e) {
			throw Ex.throwEx(e, JsonException.class);
		}
	}

	public static <T> List<T> loadFromJson(Class<T> klass, String json) {
		T[] array1 = null;
		T[] array2 = null;
		InputStream is = null;
		InputStream is2 = null;
		String json1 = null;
		String json2 = null;
		try {
			if (json.trim().startsWith("[") && json.endsWith("]")) {
				json1 = json;
			} else {
				String path = json;

				// 读取JSON文件
				if (json.startsWith("/") || json.startsWith("\\")) {
					// 从上下文环境中读取文件
					path = Demsy.contextDir + json;
					is = Files.findFileAsStream(path);
				} else {
					// 从classes目录下读取文件
					path = Demsy.appconfig.getClassDir() + "/" + json;
					is = Files.findFileAsStream(path);
					if (is == null) {// 从classes下的配置路径下读取文件
						path = Demsy.appconfig.getClassDir() + "/" + Demsy.appconfig.getConfigPkg().replace(".", "/")
								+ json;
						is = Files.findFileAsStream(path);
					}

					// 从jar包中加载JSON流
					if (is == null) {
						is = Files.findFileAsStream(json);
					}
					if (is == null) {
						path = Demsy.appconfig.getConfigPkg().replace(".", "/") + json;
						is = Files.findFileAsStream(path);
					}
					// 安装软件特有的数据
					if (Demsy.me().getSoft() != null) {
						String path2 = Demsy.appconfig.getConfigDir() + "/" + Demsy.me().getSoft().getCode() + "/"
								+ json;
						is2 = Files.findFileAsStream(path2);
					}
				}

				if (is == null && is2 == null)
					throw new ConfigException("对象配置文件不存在!");

				if (is != null)
					json1 = com.kmetop.demsy.lang.Files.readAll(is, "UTF-8");
				if (is2 != null)
					json2 = com.kmetop.demsy.lang.Files.readAll(is2, "UTF-8");
			}
			if (json1 != null)
				array1 = (T[]) Json.fromJson(Cls.forName(klass.getName() + "[]"), json1);
			if (json2 != null)
				array2 = (T[]) Json.fromJson(Cls.forName(klass.getName() + "[]"), json2);

		} catch (Throwable e) {
			throw new ConfigException("从JSON加载对象出错! [json: %s] %s", json, Ex.msg(e));
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
			if (is2 != null)
				try {
					is2.close();
				} catch (IOException e) {
				}
		}

		List<T> array = new LinkedList();
		if (array1 != null)
			for (T e : array1)
				array.add(e);
		if (array2 != null)
			for (T e : array2)
				array.add(e);

		return array;
	}
}