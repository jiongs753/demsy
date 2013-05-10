package domis.utils;

import java.util.Date;

public abstract class ClassUtil {

	/**
	 * 判断指定的类是否是基本类型？基本类型包括String、Boolean、Number。
	 * 
	 * @param cls
	 * @return
	 */
	public static boolean isBasicType(Class cls) {
		return isString(cls) || isBoolean(cls) || isNumber(cls);
	}

	public static boolean isDate(Class cls) {
		return Date.class.isAssignableFrom(cls);
	}

	public static boolean isString(Class cls) {
		return cls.equals(String.class);
	}

	public static boolean isBoolean(Class kls) {
		if (kls.equals(boolean.class) || kls.equals(Boolean.class)) {
			return true;
		}

		return false;
	}

	public static boolean isNumber(Class cls) {
		if (isInteger(cls) || isLong(cls) || isByte(cls) || isShort(cls) || isDouble(cls) || isFloat(cls) || Number.class.isAssignableFrom(cls)) {
			return true;
		}

		return false;
	}

	public static boolean isFloat(Class kls) {
		if (kls.equals(float.class) || kls.equals(Float.class)) {
			return true;
		}

		return false;
	}

	public static boolean isDouble(Class kls) {
		if (kls.equals(double.class) || kls.equals(Double.class)) {
			return true;
		}

		return false;
	}

	public static boolean isByte(Class kls) {
		if (kls.equals(byte.class) || kls.equals(Byte.class)) {
			return true;
		}

		return false;
	}

	public static boolean isShort(Class kls) {
		if (kls.equals(short.class) || kls.equals(Short.class)) {
			return true;
		}

		return false;
	}

	public static boolean isInteger(Class kls) {
		if (kls.equals(int.class) || kls.equals(Integer.class)) {
			return true;
		}

		return false;
	}

	public static boolean isLong(Class kls) {
		if (kls.equals(long.class) || kls.equals(Long.class)) {
			return true;
		}

		return false;
	}
}
