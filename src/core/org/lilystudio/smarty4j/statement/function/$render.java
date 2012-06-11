package org.lilystudio.smarty4j.statement.function;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.ParseException;
import org.lilystudio.smarty4j.expression.IExpression;
import org.lilystudio.smarty4j.expression.StringExpression;
import org.lilystudio.smarty4j.statement.LineFunction;
import org.lilystudio.smarty4j.statement.ParameterCharacter;

import com.kmetop.demsy.mvc.render.IRender;
import com.kmetop.demsy.mvc.ui.IUIView;

public class $render extends LineFunction {

	private static ParameterCharacter[] definitions = {//
	new ParameterCharacter(ParameterCharacter.OBJECT, "ui")//
			, new ParameterCharacter(ParameterCharacter.OBJECT, "uiRender") //
	};

	@Override
	public void execute(Context context, Writer writer, Object[] values) throws Exception {
		IUIView ui = (IUIView) values[0];
		IRender uiRender = (IRender) values[1];

		Map subCtx = new HashMap();
		subCtx.put("request", context.get("request"));
		subCtx.put("response", context.get("response"));
		subCtx.put("session", context.get("session"));
		subCtx.put("data", context.get("data"));

		int len = values.length;
		for (int i = 2; i < len; i += 2) {
			subCtx.put((String) values[i], values[i + 1]);
		}

		uiRender.render(writer, ui, subCtx);
	}

	@Override
	public void process(ParameterCharacter[] parameters, Map<String, IExpression> fields) throws ParseException {
		super.process(parameters, fields);
		// 移除必须存在的参数
		fields.remove("ui");
		fields.remove("uiRender");

		// 保存所有的参数与值
		IExpression[] expressions = new IExpression[fields.size() * 2 + 2];
		expressions[0] = getParameter(0);
		expressions[1] = getParameter(1);

		int i = 2;
		for (Entry<String, IExpression> entry : fields.entrySet()) {
			expressions[i] = new StringExpression(entry.getKey());
			expressions[i + 1] = entry.getValue();
			i += 2;
		}

		setParameters(expressions);
	}

	public ParameterCharacter[] getDefinitions() {
		return definitions;
	}
}