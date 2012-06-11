package com.kmetop.demsy.comlib.biz.field;

import org.nutz.json.Json;

import com.kmetop.demsy.lang.Str;

public abstract class JsonField<T> implements IExtField {

	public JsonField() {
		this("");
	}

	public JsonField(String str) {
		if (!Str.isEmpty(str)) {
			try {
				T obj = (T) Json.fromJson(this.getClass(), str);
				init(obj);
			} catch (Throwable e) {
			}
		}
	}

	protected abstract void init(T obj);

	public String toString() {
		return Json.toJson(this);
	}

}
