package org.lilystudio.smarty4j.statement.function;

import java.io.Writer;
import java.util.Map;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.ParseException;
import org.lilystudio.smarty4j.Template;
import org.lilystudio.smarty4j.expression.IExpression;
import org.lilystudio.smarty4j.statement.LineFunction;
import org.lilystudio.smarty4j.statement.ParameterCharacter;
import org.nutz.lang.Strings;

import com.kmetop.demsy.lang.Obj;

public class $optionValue extends LineFunction {

	private static ParameterCharacter[] definitions = {//
	new ParameterCharacter(ParameterCharacter.OBJECT, "bean")//
			, new ParameterCharacter(ParameterCharacter.STROBJECT, null, "prop") //
			, new ParameterCharacter(ParameterCharacter.STROBJECT, null, "pattern") //
			, new ParameterCharacter(ParameterCharacter.STRING, null, "assign") //
	};

	@Override
	public void execute(Context context, Writer writer, Object[] values) throws Exception {
		String prop = (String) values[1];
		if (Strings.isEmpty(prop)) {
			prop = "id";
		}
		Object id = Obj.getValue(values[0], prop);
		if (id == null) {
			if (values[0] == null)
				id = "";
			else
				id = values[0].toString();
		}
		if (values[3] != null) {
			context.set((String) values[3], id.toString());
		} else {
			writer.write(id.toString());
		}
	}

	@Override
	public void process(ParameterCharacter[] parameters, Map<String, IExpression> fields) throws ParseException {
		super.process(parameters, fields);
		// 移除必须存在的参数
		fields.remove("bean");
		fields.remove("name");
		fields.remove("pattern");
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