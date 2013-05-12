package soom.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import soom.log.Log;
import soom.log.Logs;


public abstract class HttpUtil {
	private static Log log = Logs.getLog(HttpUtil.class);

	public static void writeJson(HttpServletResponse response, String json) throws IOException {
		write(response, json, "text/json; charset=UTF-8");
	}

	public static void writeHtml(HttpServletResponse response, String html) throws IOException {
		write(response, html, "text/html; charset=UTF-8");
	}

	public static void writeXml(HttpServletResponse response, String xml) throws IOException {
		write(response, xml, "text/xml; charset=UTF-8");
	}

	public static void write(HttpServletResponse response, String content, String contentType) throws IOException {
		PrintWriter writer = null;
		try {
			response.setContentType(contentType);
			writer = response.getWriter();
			writer.write(content);
			writer.flush();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Throwable ex) {
					log.warn("Close PrintWriter Error!", ex);
				}
			}
		}
	}

	public static void write(HttpServletResponse response, File file) throws IOException {
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			String extName = file.getName();
			extName = extName.substring(extName.lastIndexOf(".") + 1);
			String attName = file.getName();
			response.setHeader("Content-Disposition", "attachement; filename=" + attName);
			response.setContentType("application/octet-stream");
			inStream = new FileInputStream(file);
			outStream = response.getOutputStream();
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			outStream.flush();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (Throwable ex) {
					log.warn("Close InputStream Error!", ex);
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Throwable ex) {
					log.warn("Close OutputStream Error!", ex);
				}
			}
		}
	}
}
