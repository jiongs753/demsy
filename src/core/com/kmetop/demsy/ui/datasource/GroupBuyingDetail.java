package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;

import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.common.IContact;
import com.kmetop.demsy.config.SoftConfigManager;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;

/**
 * 全文阅读栏目信息
 * 
 * @author yongshan.ji
 * 
 */
public class GroupBuyingDetail extends LoadDetailContent {

	protected void init(UIBlockContext maker, Map context) {
		super.init(maker, context);
		context.put("cfgPostFee", SoftConfigManager.me().getEshopPostFee());
		context.put("cfgNotPostFee", SoftConfigManager.me().getEshopNotPostFee());
		context.put("cfgNotPostFeeDesc", SoftConfigManager.me().getEshopNotPostFeeDesc());
		// 联系地址
		if (Demsy.me().login() != null) {
			IOrm orm = Demsy.orm();
			Class type = bizEngine.getStaticType(IContact.SYS_CODE);
			List contactList = orm.query(type, Expr.eq(LibConst.F_CREATED_BY, Demsy.me().username()).and(Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft().getId())));

			if (contactList != null && contactList.size() > 0)
				context.put("contactList", contactList);
		}
	}
}
