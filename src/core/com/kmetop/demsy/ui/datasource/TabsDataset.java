package com.kmetop.demsy.ui.datasource;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.mvc.MvcConst.URL_UI;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.mvc.MvcConst.MvcUtil;
import com.kmetop.demsy.mvc.ui.UIBlockContext;
import com.kmetop.demsy.mvc.ui.UIBlockDataModel;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.orm.expr.ExprRule;

public class TabsDataset extends UiBaseDataSource {

	public UIBlockDataModel process(UIBlockContext maker) {
		log.tracef("获取TABS数据集...");

		IBizSystem system = maker.getSystem();
		log.tracef("从业务系统获取数据. [system:%s]", system);
		if (system == null) {
			return null;
		}

		UIBlockDataModel root = maker.getCatalog();

		List<String> rules = maker.getRules();

		Map<String, IBizField> fldmap = bizEngine.getFieldsMap(system);
		String cataFldName = new ExprRule(rules.get(0)).getField();
		IBizField cataFld = fldmap.get(cataFldName);
		IBizSystem refSys = cataFld.getRefrenceSystem();
		Class refType = bizEngine.getType(refSys);
		IBizField treeFld = bizEngine.getFieldOfSelfTree(refSys);

		IOrm orm = Demsy.orm();
		if (refType != null) {
			Class dataType = maker.getType();
			List catalogs = maker.getCatalogObjs();
			int pageSize = maker.getPageSize();
			String imgfld = maker.getImageField();
			IModule catalogModule = maker.getCatalogModule();
			String[] orderBys = Str.toArray(maker.getOrderBy(), ",;");

			log.trace("计算自身树子节点作为TAB项...");
			String catSelfTree = bizEngine.getPropName(treeFld);
			List subcatas = orm.query(refType, Expr.eq(catSelfTree, catalogs.get(0)));

			for (Object catalog : subcatas) {
				log.tracef("查询TAB数据集... [catalog:%s]", catalog);

				UIBlockDataModel tabroot = new UIBlockDataModel();
				tabroot.setName(catalog.toString(), 0);
				IPage link = maker.getBlock().getTitleLink();
				if (link != null && catalog != null) {
					tabroot.setHref(MvcUtil.contextPath(URL_UI, link.getId(), catalogModule.getId() + ":" + Obj.getId(catalog)));
				}
				root.addItem(tabroot);

				// 创建条件表达式
				CndExpr expr;
				if (catalog instanceof IWebContentCatalog) {
					IWebContentCatalog webInfoCatalog = (IWebContentCatalog) catalog;
					expr = Expr.eq(cataFldName, webInfoCatalog.getId());

					IWebContentCatalog refCat = null;
					if (webInfoCatalog.getType() == IWebContentCatalog.TYPE_REF && webInfoCatalog.getRefrence() != null) {
						refCat = webInfoCatalog.getRefrence();
					}
					if (refCat != null) {
						expr = Expr.or(expr, Expr.eq(cataFldName, refCat.getId()));
					}
				} else {
					expr = Expr.eq(cataFldName, catalog);
				}
				expr.setPager(1, pageSize);
				for (String ordery : orderBys) {
					expr = expr.addOrder(Expr.orderby(ordery));
				}
				if (!Str.isEmpty(imgfld)) {
					expr = expr.and(Expr.notNull(imgfld)).and(Expr.ne(imgfld, ""));
				}

				// 创建分页器
				Pager pager = new Pager(dataType);
				pager.setQueryExpr(expr);

				// 查询
				List result = orm.query(pager);
				if (result == null) {
					result = new LinkedList();
				}

				log.tracef("查询TAB数据集. [size:%s]", result.size());

				for (int i = result.size(); i < pageSize; i++) {
					result.add(null);
				}

				for (Object obj : result) {
					if (obj == null)
						tabroot.addItem(new UIBlockDataModel());
					else
						tabroot.addItem(maker.makeDataModel(obj));
				}
			}
		}

		return root;
	}
}
