package org.lilystudio.smarty4j.statement.function;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.ParseException;
import org.lilystudio.smarty4j.Template;
import org.lilystudio.smarty4j.expression.IExpression;
import org.lilystudio.smarty4j.statement.LineFunction;
import org.lilystudio.smarty4j.statement.ParameterCharacter;

import com.kmetop.demsy.lang.Str;

public class $contains extends LineFunction {

	private static ParameterCharacter[] definitions = {//
	new ParameterCharacter(ParameterCharacter.OBJECT, "from")//
			, new ParameterCharacter(ParameterCharacter.OBJECT, "item") //
			, new ParameterCharacter(ParameterCharacter.STROBJECT, null, "key") //
			, new ParameterCharacter(ParameterCharacter.STRING, "assign") //
	};

	@Override
	public void execute(Context context, Writer writer, Object[] values) throws Exception {
		if (values[1] == null) {
			return;
		}
		List list = null;
		Map map = null;
		if (values[0] instanceof List) {
			list = (List) values[0];
		}
		if (values[0] instanceof Map) {
			map = (Map) values[0];
		}
		if (map != null) {
			if (values[2] == null || Str.isEmpty(values[2].toString())) {
				context.set((String) values[3], map.containsValue(values[1]));
			} else {
				context.set((String) values[3], map.containsKey(values[1]));
			}
		} else if (list != null) {
			context.set((String) values[3], list.contains(values[1]));
		}

	}

	@Override
	public void process(ParameterCharacter[] parameters, Map<String, IExpression> fields) throws ParseException {
		super.process(parameters, fields);
		// 移除必须存在的参数
		fields.remove("from");
		fields.remove("item");
		fields.remove("key");
		fields.remove("assign");
	}

	public ParameterCharacter[] getDefinitions() {
		return definitions;
	}

	@Override
	public void scan(Template template) {
		super.scan(template);
		template.preventAllCache();
	}
}