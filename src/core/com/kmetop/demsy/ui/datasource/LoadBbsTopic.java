package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.mvc.MvcConst.URL_UI;
import static com.kmetop.demsy.mvc.MvcConst.MvcUtil.contextPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.comlib.security.IUserRole;
import com.kmetop.demsy.comlib.web.IBbsForum;
import com.kmetop.demsy.comlib.web.IBbsReply;
import com.kmetop.demsy.comlib.web.IBbsTopic;
import com.kmetop.demsy.comlib.web.IStatistic;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.security.ILogin;

/**
 * 全文阅读栏目信息
 * 
 * @author yongshan.ji
 * 
 */
public class LoadBbsTopic extends UiRecord {

	protected Object loadRecord(UIBlockContext maker, Map context) {
		Demsy me = Demsy.me();
		IBbsTopic record = (IBbsTopic) super.loadRecord(maker, context);

		if (record == null)
			return null;

		if (maker.getPageView() != null)
			maker.getPageView().set("subtitle", record.toString());

		IBizSystem forumSys = bizEngine.getSystem(IBbsForum.SYS_CODE);
		IModule forumMdl = moduleEngine.getModule(me.getSoft(), forumSys);
		IModule commentMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem(IBbsReply.SYS_CODE));
		IModule memberMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem("demsy_web_member"));
		if (memberMdl == null || memberMdl.isDisabled()) {
			memberMdl = moduleEngine.getModule(me.getSoft(), bizEngine.getSystem(LibConst.BIZSYS_ADMIN_WEBUSER));
		}
		context.put("uploadUrl", contextPath(MvcConst.URL_UPLOAD, commentMdl.getId()));

		IBbsForum forum = (IBbsForum) Demsy.orm().load(bizEngine.getType(forumSys), record.getForum().getId());
		context.put("forum", forum);

		if (maker.getBlock().getTitleLink() != null)
			context.put("forumUrl", MvcUtil.contextPath(URL_UI, maker.getBlock().getTitleLink().getId(), forumMdl.getId() + ":" + record.getForum().getId()));

		context.put("commentUrl", MvcUtil.contextPath(MvcConst.URL_BZ_SAVE, commentMdl.getId() + ":", "c", Demsy.me().addToken()));
		Demsy.security.addPermission("block" + maker.getBlock().getId(), IUserRole.ROLE_ANONYMOUS, commentMdl.getId(), "c");

		IOrm orm = Demsy.orm();
		// 查询论坛回帖
		Integer cpage = me.param("page", Integer.class, 1);
		Pager pager = new Pager(bizEngine.getType(moduleEngine.getSystem(commentMdl)));
		CndExpr expr = Expr.asc(LibConst.F_CREATED);// = Expr.eq("hide",
													// 0).or(Expr.isNull("hide"));
		if (record != null) {
			expr = expr.and(Expr.eq("topic", record));
		}
		expr = expr.setPager(cpage, maker.getPageSize());
		pager.setQueryExpr(expr);
		orm.query(pager);

		pager.getResult().add(0, record);
		context.put("pager", pager);

		// 加载用户信息
		List<IBbsTopic> list = pager.getResult();
		List<String> usernames = new ArrayList(list.size());
		for (IBbsTopic topic : list) {
			usernames.add(topic.getCreatedBy());
		}
		Map<String, IUser> usermap = new HashMap();
		Class memType = bizEngine.getType(moduleEngine.getSystem(memberMdl));
		if (memType != null) {
			List<IUser> users = orm.query(memType, Expr.in(LibConst.F_CODE, usernames));
			for (IUser user : users) {
				usermap.put(user.getUsername(), user);
			}
		}
		String username = Demsy.me().username();
		ILogin login = Demsy.me().login();
		for (IBbsTopic topic : list) {
			IUser user = usermap.get(topic.getCreatedBy());
			if (user != null)
				topic.setAuthor(user);

			/*
			 * 计算警告信息
			 */
			/*
			 * 1. 自己永远都可以查看自己发的帖子 2. 超级管理员可以查看全部帖子
			 */
			if (login != null //
					&& (username.equals(topic.getCreatedBy())//
					|| login.getRoleType() >= IUserRole.ROLE_ADMIN_ROOT//
					)) {
			} else {
				// 检查帖子是否需要审核才能显示
				if (Str.isEmpty(topic.getWarning())) {
					byte status = topic.getStatus();
					switch (status) {
					case 0:// 未审核
						if (forum.getCheckPostStatus() == 1) {// 该论坛的需要审核后才能显示
							topic.setWarning("此贴未审核！");
						}
						break;
					case 1:// 屏蔽
						topic.setWarning("此贴已被屏蔽！");
						break;
					}
				}
				// 检查帖子查看方式
				if (Str.isEmpty(topic.getWarning())) {
					byte viewmode = topic.getViewMode();
					switch (viewmode) {
					case 1:
						if (Str.isEmpty(username)) {
							topic.setWarning("此贴只允许登录用户查看！");
						}
						break;
					case 2:
						// TODO:暂不支持
						topic.setWarning("此贴回复后才能查看！");
						break;
					case 3:
						if (Str.isEmpty(username) || !Str.toList(forum.getAdminUsers()).contains(username)) {
							topic.setWarning("此贴只有版主才能查看！");
						}
						break;
					case 4:
						if (Str.isEmpty(username) || (!Str.toList(topic.getViewUsers()).contains(username))) {
							topic.setWarning("您无权查看此贴！");
						}
						break;
					}
				}
			}
		}

		Demsy.uiEngine.addClickNum(orm, (IStatistic) record);

		return record;
	}
}
