package com.kmetop.demsy.mvc.template;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.Engine;
import org.lilystudio.smarty4j.Template;
import org.lilystudio.smarty4j.TemplateException;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Files;
import com.kmetop.demsy.lang.Str;

public class SmartyTemplate extends AbstractTemplate {
	private Map<String, Engine> engineMap;

	public SmartyTemplate() {
		engineMap = new HashMap();
	}

	private Engine getEngine(String tplDir) throws IOException {
		if (tplDir == null)
			tplDir = "";

		Engine engine = engineMap.get(tplDir);
		if (engine == null) {
			engine = new DemsyEngine();
			engine.setTemplatePath(tplDir);
			engine.setDebug(!Demsy.appconfig.isProductMode());

			engineMap.put(tplDir, engine);
		}
		return engine;
	}

	@Override
	protected void write(String templateDir, String templateName, Map obj, Writer out) throws Exception {
		if (templateName.charAt(0) == '/') {
			if (Str.isEmpty(templateDir))
				templateDir = Demsy.contextDir;
		} else {
			if (Str.isEmpty(templateDir)) {
				if (new File(Demsy.appconfig.getClassDir() + File.separator + templateName).exists()) {
					templateDir = Demsy.appconfig.getClassDir();
					templateName = File.separator + templateName;
				}
			} else {
				templateName = "/" + templateName;
			}
		}

		Engine engine = getEngine(templateDir);

		Context ctx = new Context();
		ctx.putAll(obj);

		engine.getTemplate(templateName).merge(ctx, out);
	}

	@Override
	public void renderExpression(String classPath, String expression, Map context, Writer out) throws Exception {
		DemsyEngine engine = new DemsyEngine();
		engine.setTemplatePath("");

		Context ctx = new Context();
		ctx.putAll(context);

		engine.getExpressionTemplate(classPath, expression).merge(ctx, out);
	}

	private static class DemsyEngine extends Engine {

		private Map<String, Template> classTemplates = new HashMap<String, Template>(256);

		public DemsyEngine() {
			super();
		}

		@Override
		public Template getTemplate(String name) throws IOException, TemplateException {
			String path = this.getTemplatePath() + name;
			int idx = path.indexOf("/../");
			if (idx > -1) {
				String prev = path.substring(0, idx);
				String post = path.substring(idx + 3);
				idx = prev.lastIndexOf("/");
				if (idx > -1) {
					path = prev.substring(0, idx) + post;
				}
			}

			if (this.isDebug()) {
				InputStream is = null;
				InputStreamReader isr = null;
				try {
					is = Files.findFileAsStream(path);
					if (is == null) {
						throw new IOException("模板文件不存在! [" + path + "]");
					}
					isr = new InputStreamReader(is, getEncoding());
					return new DemsyTemplate(this, isr, name);
				} finally {
					if (isr != null)
						try {
							isr.close();
						} catch (Exception e) {
						}
					if (is != null)
						try {
							is.close();
						} catch (Exception e) {
						}
				}
			}

			Template template = classTemplates.get(name);

			if (template != null) {
				return template;
			}

			InputStream is = null;
			InputStreamReader isr = null;
			try {
				is = Files.findFileAsStream(name);
				if (is == null) {
					throw new IOException("模板文件不存在! [" + name + "]");
				}
				isr = new InputStreamReader(is, getEncoding());
				template = new DemsyTemplate(this, isr, name);
				classTemplates.put(name, template);
			} finally {
				if (isr != null)
					try {
						isr.close();
					} catch (Exception e) {
					}
				if (is != null)
					try {
						is.close();
					} catch (Exception e) {
					}
			}

			return template;
		}

		public Template getExpressionTemplate(String classPath, String expression) throws IOException, TemplateException {
			StringReader isr = null;
			try {
				isr = new StringReader(expression);
				return new DemsyTemplate(this, isr, classPath);
			} finally {
				if (isr != null)
					try {
						isr.close();
					} catch (Exception e) {
					}
			}
		}
	}

	public static class DemsyTemplate extends Template {

		private String classpath;

		DemsyTemplate(Engine engine, InputStreamReader isr, String path) throws TemplateException {
			super(engine, null, isr, true);
			this.classpath = path;
		}

		DemsyTemplate(Engine engine, StringReader isr, String classpath) throws TemplateException {
			super(engine, null, isr, true);
			this.classpath = classpath;
		}

		public String getClassPath() {
			return classpath;
		}
	}
}
