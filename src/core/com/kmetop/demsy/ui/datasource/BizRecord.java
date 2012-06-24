package com.kmetop.demsy.ui.datasource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.actions.BizActions;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.ObjcetNaviNode;
import com.kmetop.demsy.mvc.nutz.DemsyObjectNodeInjector;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.model.UIBizFormModel;

public class BizRecord extends UiBaseDataSource {

	@Override
	public Object process(UIBlockContext parser) {
		Map ctx = new HashMap();
		Demsy me = Demsy.me();
		ObjcetNaviNode objNode = DemsyObjectNodeInjector.get(Demsy.servletContext, me.request(), me.response(), null,
				MvcConst.UI_BZFORM_PREFIX);

		IPageBlock block = parser.getBlock();
		IModule module = parser.getModule();
		Object data = parser.getItemObj();

		if (data == null && block.getDataset().isDynamic()) {
			IBizField fk = parser.getCatalogField();
			Object catalog = parser.getCatalogObj();
			data = Mirror.me(parser.getType()).born();
			Obj.setValue(data, Demsy.bizEngine.getPropName(fk), catalog);
		}

		if (module != null) {
			Serializable id = Obj.getId(data);
			Demsy.security.addPermission("block" + block.getId(), IUserRole.ROLE_ANONYMOUS, module.getId(),
					block.getParams());
			UIBizFormModel ret = BizActions.buildForm(block.getName(), "" + module.getId(), block.getParams() + ":"
					+ (id == null ? "" : id), objNode, MvcConst.URL_BZ_SAVE);
			ret.set("importJS", "true");

			ret.setData(data);

			ctx.put("model", ret);
		}

		ctx.put("data", data);

		return ctx;
	}
}
