package org.lilystudio.smarty4j.statement.function;

import java.io.Writer;
import java.util.Map;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.ParseException;
import org.lilystudio.smarty4j.Template;
import org.lilystudio.smarty4j.expression.IExpression;
import org.lilystudio.smarty4j.statement.LineFunction;
import org.lilystudio.smarty4j.statement.ParameterCharacter;

import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;

public class $tohtml extends LineFunction {

	private static ParameterCharacter[] definitions = {//
	new ParameterCharacter(ParameterCharacter.OBJECT, "bean")//
			, new ParameterCharacter(ParameterCharacter.STROBJECT, null, "prop") //
			, new ParameterCharacter(ParameterCharacter.STROBJECT, null, "pattern") //
			, new ParameterCharacter(ParameterCharacter.STRING, null, "assign") //
	};

	@Override
	public void execute(Context context, Writer writer, Object[] values) throws Exception {
		if (values[3] != null) {
			if (values[1] == null) {
				context.set((String) values[3], Str.toHtml(values[0]));
			} else
				context.set((String) values[3], Str.toHtml(Obj.getStringValue(values[0], (String) values[1], (String) values[2])));
		} else {
			if (values[1] == null) {
				writer.write(Str.toHtml(values[0]));
			} else {
				writer.write(Str.toHtml(Obj.getStringValue(values[0], (String) values[1], (String) values[2])));
			}
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