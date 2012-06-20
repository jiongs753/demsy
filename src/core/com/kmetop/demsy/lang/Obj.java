/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kmetop.demsy.lang;

import static com.kmetop.demsy.comlib.LibConst.F_GUID;
import static com.kmetop.demsy.comlib.LibConst.F_ID;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.entity.IBizEntity;
import com.kmetop.demsy.comlib.entity.IDynamic;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.orm.mapping.EnMapping;

public abstract class Obj {

	private static final int INITIAL_HASH = 7;
	private static final int MULTIPLIER = 31;

	private static final String NULL_STRING = "null";
	private static final String ARRAY_START = "{";
	private static final String ARRAY_END = "}";
	private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
	private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	public static boolean contains(Object[] array, Object element) {
		if (array == null) {
			return false;
		}
		for (int i = 0; i < array.length; i++) {
			if (equals(array[i], element)) {
				return true;
			}
		}
		return false;
	}

	public static Object[] add(Object[] array, Object obj) {
		Class compType = Object.class;
		if (array != null) {
			compType = array.getClass().getComponentType();
		} else if (obj != null) {
			compType = obj.getClass();
		}
		int newArrLength = (array != null ? array.length + 1 : 1);
		Object[] newArr = (Object[]) Array.newInstance(compType, newArrLength);
		if (array != null) {
			System.arraycopy(array, 0, newArr, 0, array.length);
		}
		newArr[newArr.length - 1] = obj;
		return newArr;
	}

	public static Object[] toArray(Object source) {
		if (source instanceof Object[]) {
			return (Object[]) source;
		}
		if (source == null) {
			return new Object[0];
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}
		int length = Array.getLength(source);
		if (length == 0) {
			return new Object[0];
		}
		Class wrapperType = Array.get(source, 0).getClass();
		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(source, i);
		}
		return newArray;
	}

	// ---------------------------------------------------------------------
	// Convenience methods for content-based equality/hash-code handling
	// ---------------------------------------------------------------------

	public static boolean equals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		}
		if (o1.getClass().isArray() && o2.getClass().isArray()) {
			if (o1 instanceof Object[] && o2 instanceof Object[]) {
				return Arrays.equals((Object[]) o1, (Object[]) o2);
			}
			if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
				return Arrays.equals((boolean[]) o1, (boolean[]) o2);
			}
			if (o1 instanceof byte[] && o2 instanceof byte[]) {
				return Arrays.equals((byte[]) o1, (byte[]) o2);
			}
			if (o1 instanceof char[] && o2 instanceof char[]) {
				return Arrays.equals((char[]) o1, (char[]) o2);
			}
			if (o1 instanceof double[] && o2 instanceof double[]) {
				return Arrays.equals((double[]) o1, (double[]) o2);
			}
			if (o1 instanceof float[] && o2 instanceof float[]) {
				return Arrays.equals((float[]) o1, (float[]) o2);
			}
			if (o1 instanceof int[] && o2 instanceof int[]) {
				return Arrays.equals((int[]) o1, (int[]) o2);
			}
			if (o1 instanceof long[] && o2 instanceof long[]) {
				return Arrays.equals((long[]) o1, (long[]) o2);
			}
			if (o1 instanceof short[] && o2 instanceof short[]) {
				return Arrays.equals((short[]) o1, (short[]) o2);
			}
		}
		return false;
	}

	public static int hashCode(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj.getClass().isArray()) {
			if (obj instanceof Object[]) {
				return hashCode((Object[]) obj);
			}
			if (obj instanceof boolean[]) {
				return hashCode((boolean[]) obj);
			}
			if (obj instanceof byte[]) {
				return hashCode((byte[]) obj);
			}
			if (obj instanceof char[]) {
				return hashCode((char[]) obj);
			}
			if (obj instanceof double[]) {
				return hashCode((double[]) obj);
			}
			if (obj instanceof float[]) {
				return hashCode((float[]) obj);
			}
			if (obj instanceof int[]) {
				return hashCode((int[]) obj);
			}
			if (obj instanceof long[]) {
				return hashCode((long[]) obj);
			}
			if (obj instanceof short[]) {
				return hashCode((short[]) obj);
			}
		}
		return obj.hashCode();
	}

	public static int hashCode(Object[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + hashCode(array[i]);
		}
		return hash;
	}

	public static int hashCode(boolean[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + hashCode(array[i]);
		}
		return hash;
	}

	public static int hashCode(byte[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + array[i];
		}
		return hash;
	}

	public static int hashCode(char[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + array[i];
		}
		return hash;
	}

	public static int hashCode(double[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + hashCode(array[i]);
		}
		return hash;
	}

	public static int hashCode(float[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + hashCode(array[i]);
		}
		return hash;
	}

	public static int hashCode(int[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + array[i];
		}
		return hash;
	}

	public static int hashCode(long[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + hashCode(array[i]);
		}
		return hash;
	}

	public static int hashCode(short[] array) {
		if (array == null) {
			return 0;
		}
		int hash = INITIAL_HASH;
		int arraySize = array.length;
		for (int i = 0; i < arraySize; i++) {
			hash = MULTIPLIER * hash + array[i];
		}
		return hash;
	}

	public static int hashCode(boolean bool) {
		return bool ? 1231 : 1237;
	}

	public static int hashCode(double dbl) {
		long bits = Double.doubleToLongBits(dbl);
		return hashCode(bits);
	}

	public static int hashCode(float flt) {
		return Float.floatToIntBits(flt);
	}

	public static int hashCode(long lng) {
		return (int) (lng ^ (lng >>> 32));
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return NULL_STRING;
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		if (obj instanceof Object[]) {
			return toString((Object[]) obj);
		}
		if (obj instanceof boolean[]) {
			return toString((boolean[]) obj);
		}
		if (obj instanceof byte[]) {
			return toString((byte[]) obj);
		}
		if (obj instanceof char[]) {
			return toString((char[]) obj);
		}
		if (obj instanceof double[]) {
			return toString((double[]) obj);
		}
		if (obj instanceof float[]) {
			return toString((float[]) obj);
		}
		if (obj instanceof int[]) {
			return toString((int[]) obj);
		}
		if (obj instanceof long[]) {
			return toString((long[]) obj);
		}
		if (obj instanceof short[]) {
			return toString((short[]) obj);
		}

		if (obj instanceof IBizEntity)
			return Cls.getType(obj.getClass()).getSimpleName() + "#" + ((IBizEntity) obj).getId();
		else
			return Cls.getType(obj.getClass()).getSimpleName() + "@" + Integer.toHexString(obj.hashCode());
	}

	public static String toString(Object[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(ARRAY_START);
			} else {
				buffer.append(ARRAY_ELEMENT_SEPARATOR);
			}
			buffer.append(String.valueOf(array[i]));
		}
		buffer.append(ARRAY_END);
		return buffer.toString();
	}

	public static String toString(boolean[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(ARRAY_START);
			} else {
				buffer.append(ARRAY_ELEMENT_SEPARATOR);
			}

			buffer.append(array[i]);
		}
		buffer.append(ARRAY_END);
		return buffer.toString();
	}

	public static String toString(byte[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(ARRAY_START);
			} else {
				buffer.append(ARRAY_ELEMENT_SEPARATOR);
			}
			buffer.append(array[i]);
		}
		buffer.append(ARRAY_END);
		return buffer.toString();
	}

	public static String toString(char[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(ARRAY_START);
			} else {
				buffer.append(ARRAY_ELEMENT_SEPARATOR);
			}
			buffer.append("'").append(array[i]).append("'");
		}
		buffer.append(ARRAY_END);
		return buffer.toString();
	}

	public static String toString(double[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(ARRAY_START);
			} else {
				buffer.append(ARRAY_ELEMENT_SEPARATOR);
			}

			buffer.append(array[i]);
		}
		buffer.append(ARRAY_END);
		return buffer.toString();
	}

	public static String toString(float[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(ARRAY_START);
			} else {
				buffer.append(ARRAY_ELEMENT_SEPARATOR);
			}

			buffer.append(array[i]);
		}
		buffer.append(ARRAY_END);
		return buffer.toString();
	}

	public static String toString(int[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(ARRAY_START);
			} else {
				buffer.append(ARRAY_ELEMENT_SEPARATOR);
			}
			buffer.append(array[i]);
		}
		buffer.append(ARRAY_END);
		return buffer.toString();
	}

	public static String toString(long[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(ARRAY_START);
			} else {
				buffer.append(ARRAY_ELEMENT_SEPARATOR);
			}
			buffer.append(array[i]);
		}
		buffer.append(ARRAY_END);
		return buffer.toString();
	}

	public static String toString(short[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(ARRAY_START);
			} else {
				buffer.append(ARRAY_ELEMENT_SEPARATOR);
			}
			buffer.append(array[i]);
		}
		buffer.append(ARRAY_END);
		return buffer.toString();
	}

	public static <T> T getValue(Object obj, final String path) {
		if (obj == null) {
			return null;
		}

		if (Str.isEmpty(path))
			return null;

		int dot = path.indexOf(".");
		if (dot > -1) {
			Object subObj = null;
			try {
				if (obj instanceof Map) {
					subObj = ((Map) obj).get(path.substring(0, dot));
				} else {
					subObj = Mirror.me(obj).getValue(obj, path.substring(0, dot));
				}
			} catch (Throwable e) {
				try {
					if (obj instanceof IDynamic) {
						return (T) ((IDynamic) obj).get(path);
					}
				} catch (Throwable iglore) {
				}
			}
			if (subObj == null) {
				return null;
			}

			return (T) getValue(subObj, path.substring(dot + 1));
		} else {
			try {
				return (T) Mirror.me(obj).getValue(obj, path);
			} catch (Throwable e) {
				try {
					if (obj instanceof IDynamic) {
						return (T) ((IDynamic) obj).get(path);
					}
				} catch (Throwable iglore) {
				}
				return null;
			}
		}
	}

	public static void setValue(Object obj, String path, Object value) {
		if (obj == null) {
			return;
		}
		Mirror me = Mirror.me(obj);
		try {
			Class type = me.getField(path).getType();
			if (Obj.isEntity(value) && !Cls.isEntityType(type)) {
				if (Number.class.isAssignableFrom(type)) {
					value = Obj.getId(value);
				} else if (String.class.equals(type)) {
					value = getValue(value, LibConst.F_GUID);
				}
			}

			int dot = path.indexOf(".");
			if (dot < 0) {
				me.setValue(obj, path, value);
			} else {
				String prop = path.substring(0, dot);
				Class fldtype = me.getField(prop).getType();
				Mirror fldme = Mirror.me(fldtype);
				Object fldval = fldme.born();
				setValue(fldval, path.substring(dot + 1), value);
				setValue(obj, prop, fldval);
			}
		} catch (NoSuchFieldException e) {
		}
	}

	public static String getStringValue(Object obj, String path) {
		return Obj.format(getValue(obj, path), null);
	}

	public static String getStringValue(Object bean, String path, String pattern) {
		if (bean == null || Str.isEmpty(path)) {
			return "";
		}

		return Obj.format(getValue(bean, path), pattern);
	}

	public static boolean isAgent(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Class) {
			return Cls.isLazy((Class) obj);
		}
		return Cls.isAgent(obj.getClass());
	}

	public static boolean isEntity(Object obj) {
		if (obj == null) {
			return false;
		}
		return Cls.isEntityType(obj.getClass());
	}

	public static boolean isEmpty(Object obj) {
		return Obj.isEmpty(null, obj);
	}

	public static boolean isEmpty(EnMapping mapping, Object obj) {
		Serializable id = Obj.getId(mapping, obj);
		if (id instanceof Number) {
			return ((Number) id).intValue() <= 0;
		}
		if (id instanceof String) {
			return id == null || id.toString().trim().length() < 0;
		}
		return id == null;
	}

	public static Serializable getId(Object obj, String idField) {
		if (Str.isEmpty(idField)) {
			return Obj.getId(obj);
		}
		return getValue(obj, idField);
	}

	public static Serializable getId(Object obj) {
		if (obj instanceof IBizEntity) {
			return ((IBizEntity) obj).getId();
		}
		return getValue(obj, F_ID);
	}

	public static Serializable getId(EnMapping mapping, Object obj) {
		if (obj instanceof IBizEntity) {
			return ((IBizEntity) obj).getId();
		}
		String field = F_ID;
		if (mapping != null) {
			field = mapping.getIdProperty();
		}

		if (field == null)
			return null;

		return getValue(obj, field);
	}

	public static void setId(EnMapping mapping, Object obj, Serializable id) {
		if (obj instanceof IBizEntity) {
			((IBizEntity) obj).setId((Long) id);
			return;
		}
		String field = F_ID;
		if (mapping != null) {
			field = mapping.getIdProperty();
		}

		if (field != null)
			setValue(obj, field, id);
	}

	public static boolean checkGuid(Object obj, String guid) {
		if (Str.isEmpty(guid)) {
			// throw new DemsyException("GUID不能为空! %s", Langs.toJson(obj));
			return false;
		}
		Object old = Demsy.orm().load(obj.getClass(), Expr.eq(F_GUID, guid));
		if (old != null && !old.equals(obj)) {
			// throw new DemsyException("GUID已经存在! %s", Langs.toJson(obj));
			return false;
		}

		return true;
	}

	public static void makeSetupGuid(Object obj, String... code) {
		StringBuffer sb = new StringBuffer();
		for (String c : code) {
			sb.append("_").append(c);
		}

		String guid = Cls.getType(obj.getClass()).getSimpleName() + sb.toString();
		guid = guid.replace(".", "_");
		guid = guid.toUpperCase();

		if (checkGuid(obj, guid))
			setValue(obj, F_GUID, guid);

	}

	public static String format(Object value, String pattern) {
		if (pattern != null && pattern.startsWith("*")) {
			if (Demsy.appconfig.isPrivacyMode()) {
				return Str.secretStr(value == null ? "" : value.toString());
			} else {
				pattern = pattern.substring(1);
			}
		}
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Date) {
			if (!Str.isEmpty(pattern)) {
				try {
					return Dates.formatDate((Date) value, pattern);
				} catch (Throwable e) {
					return Dates.formatDateTime((Date) value);
				}
			} else {
				return Dates.formatDate((Date) value);
			}
		} else if (value instanceof Number && !Str.isEmpty(pattern)) {
			// if (Strings.isEmpty(pattern)) {
			// if (value instanceof Long || value instanceof Integer ||
			// value instanceof Short)
			// pattern = "#,##0";
			// else
			// pattern = "#,##0.0#";
			// }
			return new DecimalFormat(pattern).format(new BigDecimal(value.toString()));
		}

		if (value instanceof List) {
			List list = (List) value;
			if (list.size() == 0) {
				return "";
			} else {
				StringBuffer sb = new StringBuffer();
				for (Object obj : list) {
					sb.append(",").append(obj);
				}
				return sb.substring(1);
			}
		}

		return value == null ? "" : value.toString();
	}

	/**
	 * 将指定字段转换成下拉选项键值
	 * 
	 * @param obj
	 * @param prop
	 * @return
	 */
	public static String toKey(Object obj, String prop) {
		if (obj == null) {
			return "";
		}
		if (Cls.isBoolean(obj.getClass())) {
			if ((Boolean) obj) {
				return "1";
			} else {
				return "0";
			}
		}
		if (Cls.isSimpleType(obj.getClass())) {
			return obj.toString();
		}
		try {
			Object v = Mirror.me(obj.getClass()).getValue(obj, prop == null || prop.trim().length() == 0 ? "id" : prop);
			if (v == null) {
				return "";
			}
			return v.toString();
		} catch (Throwable e) {
			return obj.toString();
		}
	}

	/**
	 * 生成下拉选项的键值
	 * 
	 * @param obj
	 * @return
	 */
	public static String toKey(Object obj) {
		return toKey(obj, null);
	}

	/**
	 * 判断指定的数字是否为正数
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isPositive(Number cs) {
		if (null != cs && cs.intValue() > 0)
			return true;

		return false;
	}

	public static boolean isTrue(Boolean b) {
		if (b != null)
			return b.booleanValue();

		return false;
	}

}
