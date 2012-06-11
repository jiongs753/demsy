package com.kmetop.demsy.mvc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.lang.inject.Injecting;
import org.nutz.mvc.adaptor.ParamConvertor;
import org.nutz.mvc.adaptor.Params;

import com.kmetop.demsy.comlib.entity.IDynamic;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Obj;

public class ObjcetNaviNode {
	private static final char separator = '.';

	private String name;

	// 叶子节点的值
	private String[] value;

	// 是否是叶子节点
	private boolean leaf = true;

	// 子节点
	private Map<String, ObjcetNaviNode> child = new HashMap<String, ObjcetNaviNode>();

	/**
	 * 初始化当前结点
	 * 
	 */
	public void put(String path, String[] value) {
		name = fetchName(path);
		String subPath = path.substring(path.indexOf(separator) + 1);
		if (path.indexOf(separator) <= 0 || "".equals(subPath)) {
			this.value = value;
			return;
		}
		leaf = false;
		addChild(subPath, value);
	}

	/**
	 * 添加子结点
	 * 
	 */
	private void addChild(String path, String[] value) {
		String subname = fetchName(path);
		ObjcetNaviNode onn = child.get(subname);
		if (onn == null) {
			onn = new ObjcetNaviNode();
		}
		onn.put(path, value);
		child.put(subname, onn);
	}

	/**
	 * 取得节点名
	 * 
	 */
	private String fetchName(String path) {
		if (path.indexOf(separator) <= 0) {
			return path;
		}
		return path.substring(0, path.indexOf(separator));
	}

	public Map injectMap(Mirror mirror, Map<String, String> fieldMode) {
		Map ret = new HashMap();
		Iterator<String> keys = child.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next().trim();
			if (key.startsWith("[") && key.endsWith("]")) {
				key = key.substring(1, key.length() - 1);
				ret.put(key, child.get(key).inject(mirror, null, fieldMode));
			}
		}

		return ret;
	}

	public List injectList(Mirror mirror, Map<String, String> fieldMode) {
		List list = new LinkedList();
		Iterator<String> keys = child.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next().trim();
			if (key.startsWith("[") && key.endsWith("]")) {
				list.add(child.get(key).inject(mirror, null, fieldMode));
			}
		}

		return list;
	}

	public Object inject(Mirror mirror, Object obj, Map<String, String> fieldMode) {
		return inject(mirror, obj, null, fieldMode);
	}

	/**
	 * 将结点树中的值注入到 mirror 中
	 * 
	 */
	public Object inject(Mirror mirror, Object obj, Class genericType, Map<String, String> fieldMode) {
		if (obj == null) {
			obj = mirror.born();
		}
		for (Entry<String, ObjcetNaviNode> entry : child.entrySet()) {
			ObjcetNaviNode onn = entry.getValue();
			String[] values = onn.getValue();
			String key = entry.getKey();
			if (fieldMode != null) {
				String mode = fieldMode.get(key);
				if (!Strings.isEmpty(mode) && !"M".equals(mode) && !"E".equals(mode) && !"H".equals(mode)) {
					continue;
				}
			}
			if (onn.isLeaf()) {
				try {
					if (obj instanceof List) {
						List list = (List) obj;
						for (String value : values) {
							Mirror itemMirror = Mirror.me(genericType);
							Object item = itemMirror.born();
							Injecting in = itemMirror.getInjecting(key);
							ParamConvertor pc = Params.makeParamConvertor(itemMirror.getField(entry.getKey()).getType());
							in.inject(item, pc.convert(new String[] { value }));
							list.add(item);
						}
					} else {
						Injecting in = mirror.getInjecting(key);
						Class type = mirror.getField(entry.getKey()).getType();
						ParamConvertor pc = Params.makeParamConvertor(type);
						if (Number.class.isAssignableFrom(type)) {
							values[0] = values[0].replace(",", "");
						}
						in.inject(obj, pc.convert(new String[] { values[0] }));
					}
				} catch (RuntimeException e) {
					Method m = null;
					try {
						m = mirror.getSetter(key, String.class);
					} catch (Throwable e1) {
					}
					if (m == null) {
						if (obj instanceof IDynamic && values != null && values.length > 0) {
							((IDynamic) obj).set(key, values[0]);
						}
					} else {
						throw e;
					}
				} catch (Throwable e) {
					if (obj instanceof IDynamic && values != null && values.length > 0) {
						((IDynamic) obj).set(key, values[0]);
					}
				}
				continue;
			}

			try {
				// 不是叶子结点,不能直接注入
				Injecting in = mirror.getInjecting(key);
				Field field = mirror.getField(entry.getKey());
				Class type = field.getType();
				if (type.equals(List.class)) {
					String typeStr = field.getGenericType().toString();
					int idx = typeStr.indexOf("<");
					typeStr = typeStr.substring(idx + 1);
					typeStr = typeStr.substring(0, typeStr.length() - 1);
					Mirror<?> fieldMirror = Mirror.me(LinkedList.class);
					in.inject(obj, onn.inject(fieldMirror, null, Cls.forName(typeStr), null));
				} else {
					Mirror<?> fieldMirror = Mirror.me(type);
					Object next = null;
					if (!Obj.isEntity(type)) {
						next = mirror.getValue(obj, key);
					}
					in.inject(obj, onn.inject(fieldMirror, next, fieldMode));
				}
			} catch (Throwable e) {
				if (obj instanceof IDynamic) {
					onn.injectDynamic((IDynamic) obj, key);
				}
				continue;
			}
		}
		return obj;
	}

	private Object injectDynamic(IDynamic obj, String path) {
		for (Entry<String, ObjcetNaviNode> entry : child.entrySet()) {
			ObjcetNaviNode onn = entry.getValue();
			String[] value = onn.getValue();
			String key = entry.getKey();
			if (onn.isLeaf()) {
				if (value != null && value.length > 0) {
					obj.set(path + "." + key, value[0]);
				} else {
					obj.set(path + "." + key, "");
				}
			} else {
				onn.injectDynamic(obj, path + "." + key);
			}
		}
		return obj;
	}

	public int size() {
		return child.size();
	}

	public String[] getValue() {
		return value;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public String getName() {
		return name;
	}
}