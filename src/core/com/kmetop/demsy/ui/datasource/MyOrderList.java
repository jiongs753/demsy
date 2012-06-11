package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.mvc.MvcConst.URL_UI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.actions.OrderActions;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.comlib.eshop.IOrderItem;
import com.kmetop.demsy.comlib.eshop.IProduct;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

public class MyOrderList extends UiBaseDataSource {

	protected CndExpr getExpr(UIBlockContext parser) {
		return null;
	}

	public Map process(UIBlockContext parser) {
		Map ctx = new HashMap();

		UIBlockDataModel data = parser.getCatalog();

		IDemsySoft soft = Demsy.me().getSoft();
		ctx.put("data", data);

		// 用于产生订单条目产品链接
		IPageBlock block = parser.getBlock();
		Long moduleID = null;
		IModule module = moduleEngine.getModule(soft, bizEngine.getSystem(IOrder.SYS_CODE));
		if (module != null)
			moduleID = module.getId();
		Long linkID = null;
		if (block.getTitleLink() != null)
			linkID = block.getTitleLink().getId();
		Class orderClass = bizEngine.getSystemClass(IOrder.SYS_CODE);

		Long pmoduleID = null;
		IModule pmodule = moduleEngine.getModule(soft, bizEngine.getSystem(IProduct.SYS_CODE));
		if (pmodule != null)
			pmoduleID = pmodule.getId();
		Long plinkID = null;
		if (block.getLink() != null)
			plinkID = block.getLink().getId();
		Class itemClass = bizEngine.getSystemClass(IOrderItem.SYS_CODE);

		// 订单列表
		IOrm orm = Demsy.orm();
		List<IOrder> orders = null;
		String person = Demsy.me().param("orderPerson", String.class, "");
		String telcode = Demsy.me().param("orderTelcode", String.class, "");
		String status = Demsy.me().param("orderStatus", String.class, "");
		if (Str.isEmpty(telcode) && Str.isEmpty(status)) {
			orders = OrderActions.getMyOrders(parser.getDynamicDataID());
		} else {
			String user = Demsy.me().username();
			CndExpr expr = Expr.eq(LibConst.F_SOFT_ID, soft.getId()).addDesc(LibConst.F_ID);
			if (!Str.isEmpty(telcode)) {
				if (!Str.isEmpty(person)) {
					expr = expr.and(Expr.eq("code", telcode)).and(Expr.eq("name", person));
				} else {
					expr = expr.and(Expr.eq("code", telcode).or(Expr.eq(LibConst.F_TIME_ID, telcode)));
				}
				if (!Str.isEmpty(status)) {
					expr = expr.and(Expr.eq("status", Byte.parseByte(status)));
				}
				orders = orm.query(orderClass, expr);
			} else if (!Str.isEmpty(user)) {
				expr = expr.and(Expr.eq(LibConst.F_CREATED_BY, user));
				if (!Str.isEmpty(status)) {
					expr = expr.and(Expr.eq("status", Byte.parseByte(status)));
				}
				orders = orm.query(orderClass, expr);
			}
			ctx.put("orderTelcode", telcode);
			if (!Str.isEmpty(person))
				ctx.put("orderPerson", person);
		}
		if (orders != null) {
			for (IOrder order : orders) {
				UIBlockDataModel orderData = makeOrderData(block, order, linkID, moduleID);
				data.addItem(orderData);
				List<IOrderItem> items = orm.query(itemClass, Expr.eq("order", order));
				for (IOrderItem item : items) {
					orderData.addItem(makeItemData(block, item, plinkID, pmoduleID));
				}
			}
		}
		ctx.put("orderStatus", status);
		ctx.put("orderModule", module);

		return ctx;
	}

	public UIBlockDataModel makeOrderData(IPageBlock block, IOrder obj, Long linkID, Long moduleID) {
		UIBlockDataModel item = new UIBlockDataModel();

		item.setName(obj.toString());

		if (linkID != null && moduleID != null) {
			item.setHref(MvcUtil.contextPath(URL_UI, linkID, moduleID + ":" + obj.getId()));
		}
		if (!Str.isEmpty(block.getTitleLinkTarget())) {
			item.setTarget(block.getTitleLinkTarget());
		}

		item.setObj(obj);

		return item;
	}

	public UIBlockDataModel makeItemData(IPageBlock block, IOrderItem obj, Long linkID, Long moduleID) {
		UIBlockDataModel item = new UIBlockDataModel();

		item.setName(obj.toString());

		IProduct p = obj.getProduct();
		if (p != null && p.getImage() != null)
			item.setImg(Demsy.contextPath + p.getImage().toString());

		if (linkID != null && moduleID != null && p != null) {
			item.setHref(MvcUtil.contextPath(URL_UI, linkID, moduleID + ":" + p.getId()));
		}
		if (!Str.isEmpty(block.getLinkTarget())) {
			item.setTarget(block.getLinkTarget());
		}

		item.setObj(obj);

		return item;
	}
}
