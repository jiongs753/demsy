package com.kmetop.demsy.ui.datasource;

import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.actions.BizActions;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.model.UIBizFormModel;

public class MyInfo extends UiBaseDataSource {

	@Override
	public Object process(UIBlockContext parser) {
		Map ctx = new HashMap();

		IPageBlock block = parser.getBlock();
		IModule module = parser.getModule();
		IUser user = Demsy.me().loginUser();

		Class type = Demsy.bizEngine.getType(Demsy.moduleEngine.getSystem(module));
		if (type == null || !Cls.getType(type).getName().equals(Cls.getType(user.getClass()).getName())) {
			return ctx;
		}

		if (module != null) {

			if (user != null) {
				Demsy.security.addPermission("block" + block.getId(), IUserRole.ROLE_ANONYMOUS, module.getId(),
						block.getParams());
				UIBizFormModel ret = BizActions.buildForm(block.getName(), "" + module.getId(), block.getParams() + ":"
						+ user.getId(), null, MvcConst.URL_BZ_SAVE);
				ret.set("importJS", "true");

				user = (IUser) ret.getData();
				Demsy.me().login().setUser(user);
				ret.setData(user);

				ctx.put("model", ret);
			}
		}

		ctx.put("data", user);

		return ctx;
	}

}
