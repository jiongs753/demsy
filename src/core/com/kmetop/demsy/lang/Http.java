package com.kmetop.demsy.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.kmetop.demsy.Demsy;

public abstract class Http {
	public static void writeHtml(String html) throws IOException {
		write(html, "text/html; charset=UTF-8");
	}

	public static void writeXml(String xml) throws IOException {
		write(xml, "text/xml; charset=UTF-8");
	}

	public static void write(String content, String contentType) throws IOException {
		PrintWriter writer = null;
		try {
			HttpServletResponse response = Demsy.me().response();
			response.setContentType(contentType);
			writer = response.getWriter();
			writer.write(content);
			writer.flush();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Throwable ex) {
				}
			}
		}
	}

	public static void write(File file) throws IOException {
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			HttpServletResponse response = Demsy.me().response();
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
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Throwable ex) {
				}
			}
		}
	}
}
