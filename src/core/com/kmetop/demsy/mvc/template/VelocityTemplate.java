package com.kmetop.demsy.mvc.template;

import java.io.InputStream;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Files;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class VelocityTemplate extends AbstractTemplate {
	private static Log log = Logs.getLog(VelocityTemplate.class);

	public VelocityTemplate() {
		Properties p = new Properties();
		p.put("file.resource.loader.class", DemsyVelocityResourceLoader.class.getName());
		try {
			Velocity.init(p);
		} catch (Exception e) {
			log.errorf("初始化Velocity出错! %s", Ex.msg(e));
		}
	}

	@Override
	protected void write(String templateDir, String templateName, Map root, Writer out) throws Exception {
		VelocityContext context = new VelocityContext(root);

		Velocity.getTemplate(templateName).merge(context, out);
	}

	public static class DemsyVelocityResourceLoader extends ResourceLoader {

		@Override
		public long getLastModified(Resource res) {
			return 0;
		}

		@Override
		public InputStream getResourceStream(String templateName) throws ResourceNotFoundException {
			if (templateName.charAt(0) == '/')
				return Files.findFileAsStream(Demsy.contextDir + templateName);
			else
				return Files.findFileAsStream(templateName);

		}

		@Override
		public boolean isSourceModified(Resource res) {
			return false;
		}

		@Override
		public void init(ExtendedProperties arg0) {
			
		}

	}
}
