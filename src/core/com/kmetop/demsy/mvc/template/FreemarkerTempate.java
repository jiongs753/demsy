package com.kmetop.demsy.mvc.template;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Str;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerTempate extends AbstractTemplate {
	private Map<String, Configuration> cfgMap;

	public FreemarkerTempate() {
		cfgMap = new HashMap();
	}

	private Configuration getConfig(String templateDir) throws IOException {
		Configuration cfg = cfgMap.get(templateDir);
		if (cfg == null) {
			cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(templateDir));
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			cfg.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));

			cfgMap.put(templateDir, cfg);
		}
		return cfg;
	}

	@Override
	protected void write(String templateDir, String templateName, Map root, Writer out) throws Exception {
		if (templateName.charAt(0) == '/') {
			if (Str.isEmpty(templateDir))
				templateDir = Demsy.contextDir;
		} else {
			if (Str.isEmpty(templateDir)) {
				String classPath = Demsy.contextDir + "/WEB-INF/classes";
				String name = "/" + templateName;
				if (new File(classPath + name).exists()) {
					templateDir = classPath;
					templateName = name;
				}
			} else {
				templateName = "/" + templateName;
			}
		}

		Template tmpl = getConfig(templateDir).getTemplate(templateName);

		tmpl.process(root, out);
	}

}
