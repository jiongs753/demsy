package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.mvc.MvcConst.MvcUtil.contextPath;

import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.web.IBlogPost;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;

public class LoadBlogList extends UiDataset {

	public Map process(UIBlockContext parser) {
		Map context = super.process(parser);

		IModule topicMdl = moduleEngine.getModule(Demsy.me().getSoft(), bizEngine.getSystem(IBlogPost.SYS_CODE));
		context.put("topicUrl", MvcUtil.contextPath(MvcConst.URL_BZ_SAVE, topicMdl.getId() + ":", "c", Demsy.me().addToken()));
		Demsy.security.addPermission("block" + parser.getBlock().getId(), IUserRole.ROLE_ANONYMOUS, topicMdl.getId(), "c");
		context.put("uploadUrl", contextPath(MvcConst.URL_UPLOAD, topicMdl.getId()));

		if (parser.getPageView() != null)
			parser.getPageView().set("subtitle", "博客文章");

		return context;
	}
}
