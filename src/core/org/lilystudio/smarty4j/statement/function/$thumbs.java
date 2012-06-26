package org.lilystudio.smarty4j.statement.function;

import java.io.File;
import java.io.Writer;
import java.util.Map;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.ParseException;
import org.lilystudio.smarty4j.Template;
import org.lilystudio.smarty4j.expression.IExpression;
import org.lilystudio.smarty4j.statement.LineFunction;
import org.lilystudio.smarty4j.statement.ParameterCharacter;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.Img;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;

public class $thumbs extends LineFunction {
	protected Log log = Logs.get();

	private static ParameterCharacter[] definitions = {//
	new ParameterCharacter(ParameterCharacter.OBJECT, "src")//
			, new ParameterCharacter(ParameterCharacter.INTOBJECT, "width") //
			, new ParameterCharacter(ParameterCharacter.INTOBJECT, "height") //
			, new ParameterCharacter(ParameterCharacter.STRING, null, "assign") //
			, new ParameterCharacter(ParameterCharacter.STRING, null, "press") //
			, new ParameterCharacter(ParameterCharacter.BOOLOBJECT, null, "autoCut") //
	};

	@Override
	public void execute(Context context, Writer writer, Object[] values) throws Exception {
		try {
			if (values[0] == null) {
				if (values[3] != null)
					context.set((String) values[3], "");
				return;
			}
			if (values[5] == null) {
				values[5] = false;
			}
			String src = (String) values[0].toString().trim();
			if (!Img.isImage(src)) {
				if (values[3] != null)
					context.set((String) values[3], "");
				return;
			}
			int w = (Integer) values[1];
			int h = (Integer) values[2];

			String localSrc = Str.dencodeUri(src);
			if (localSrc.startsWith("http://")) {
				localSrc = localSrc.substring(7);
				localSrc = localSrc.substring(localSrc.indexOf("/"));
			}
			if (localSrc.startsWith("https://")) {
				localSrc = localSrc.substring(8);
				localSrc = localSrc.substring(localSrc.indexOf("/"));
			}

			String zoomImg = Img.zoomImgPath(localSrc, w, h).replace(" ", "_");
			String targetImg = Demsy.contextDir + zoomImg;
			File zoomFile = new File(targetImg);

			if (!zoomFile.exists()) {
				Img.zoomImage(Demsy.contextDir + localSrc, targetImg, w, h, (Boolean) values[5]);
				String pressImg = Demsy.contextDir + MvcUtil.getUploadBasePath() + "/images/logo.png";
				if (new File(pressImg).exists() && new File(targetImg).exists() && "true".equals(values[4]))
					Img.pressImage(pressImg, targetImg, 10, 10);
			}
			if (zoomFile.exists()) {
				zoomImg = Str.encodeUri(zoomImg);
			} else {
				zoomImg = src;
			}

			if (values[3] != null)
				context.set((String) values[3], zoomImg);
			else
				writer.write(zoomImg);
		} catch (Throwable e) {
			log.error(e);
		}
	}

	@Override
	public void process(ParameterCharacter[] parameters, Map<String, IExpression> fields) throws ParseException {
		super.process(parameters, fields);
		// 移除必须存在的参数
		fields.remove("src");
		fields.remove("width");
		fields.remove("height");
		fields.remove("assign");
		fields.remove("press");
		fields.remove("autoCut");
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