package com.kmetop.demsy.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Props {
	public static boolean boolValue(Properties prop, String key) {
		try {
			return Boolean.parseBoolean(stringValue(prop, key));
		} catch (Throwable e) {
			return false;
		}
	}

	public static byte byteValue(Properties prop, String key) {
		try {
			return Byte.parseByte(stringValue(prop, key));
		} catch (Throwable e) {
			return 0;
		}
	}

	public static int intValue(Properties prop, String key) {
		try {
			return Integer.parseInt(stringValue(prop, key));
		} catch (Throwable e) {
			return 0;
		}
	}

	public static String stringValue(Properties prop, String key) {
		return prop.getProperty(key);
	}

	public static void put(Properties prop, String key, Object value) {
		prop.put(key, value.toString());
	}

	public static Properties toProps(String str) throws IOException {
		Properties prop = new JsonProperties();
		InputStream inStream = null;
		try {
			if (str != null) {
				inStream = new ByteArrayInputStream(str.getBytes());
				// prop.loadFromXML(inStream);
				prop.load(inStream);
			}
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
				}
			}
		}
		return prop;
	}

	public static String toString(Properties prop) throws IOException {
		if (prop == null) {
			return null;
		}
		OutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			// prop.storeToXML(out, "");
			prop.store(out, null);
			return out.toString();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static class JsonProperties extends Properties {
		private static final long serialVersionUID = -4060680147697578500L;

		@Override
		public synchronized Object get(Object key) {
			return super.get(key);
		}

		@Override
		public synchronized Object put(Object key, Object value) {
			if (value instanceof String[]) {
				String[] array = (String[]) value;
				if (array.length > 0) {
					value = array[0];
				}
			}
			if (value == null || Str.isEmpty(value.toString())) {
				this.remove(key);
				return null;
			} else {
				return super.put(key, value.toString());
			}
		}

	}
}
