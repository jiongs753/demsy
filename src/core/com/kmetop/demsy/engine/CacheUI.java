package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_PAGE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_PAGE_BLOCK;
import static com.kmetop.demsy.comlib.LibConst.F_ORDER_BY;
import static com.kmetop.demsy.comlib.LibConst.*;

import java.util.List;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.orm.expr.Expr;

class CacheUI {

	private IPage page;

	private List<? extends IPageBlock> blocks;

	CacheUI(UiEngine engine, Long pageID) {
		if (pageID != null) {
			page = (IPage) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_UIUDF_PAGE), pageID);

			if (page != null) {
				blocks = Demsy.orm().query(bizEngine.getStaticType(BIZSYS_UIUDF_PAGE_BLOCK), Expr.eq(F_UI_PAGE, page).and(Expr.eq(F_DISABLED, false)).addAsc(F_ORDER_BY));

				if (Demsy.appconfig.isProductMode()) {
					engine.pageCache.put(pageID, this);
					for (IPageBlock block : blocks) {
						engine.pageBlockCache.put(block.getId(), block);
					}
				}
			}
		}
	}

	IPage get() {
		return page;
	}

	List<? extends IPageBlock> blocks() {
		return blocks;
	}
}
