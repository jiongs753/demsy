package org.lilystudio.smarty4j.statement.function;

import java.io.Writer;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.util.StringWriter;

import com.kmetop.demsy.mvc.template.SmartyTemplate.DemsyTemplate;

public class $import extends $include {

	@Override
	public void execute(Context context, Writer writer, Object[] values) throws Exception {
		Object assign = values[1];
		if (assign != null) {
			writer = new StringWriter();
		}

		// 加载子模板, 设置子模板的父容器
		DemsyTemplate template = (DemsyTemplate) context.getTemplate();
		String name = (String) values[0];
		String parentPath = template.getClassPath();
		int idx = parentPath.lastIndexOf("/");
		if (idx > -1) {
			parentPath = parentPath.substring(0, idx);
		}
		int upidx = name.indexOf("../");
		while (upidx > -1) {
			idx = parentPath.lastIndexOf("/");
			if (idx > -1) {
				parentPath = parentPath.substring(0, idx);
			}
			name = name.substring(3);
			upidx = name.indexOf("../");
		}
		name = parentPath + "/" + name;

		template = (DemsyTemplate) template.getEngine().getTemplate(name);
		Context childContext = new Context(context);

		int len = values.length;
		for (int i = 2; i < len; i += 2) {
			childContext.set((String) values[i], values[i + 1]);
		}

		template.merge(childContext, writer);

		if (assign != null) {
			context.set((String) assign, writer.toString());
		}
	}
}
