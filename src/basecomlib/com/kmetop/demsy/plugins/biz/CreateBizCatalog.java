package com.kmetop.demsy.plugins.biz;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.biz.IBizCatalog;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.plugins.BizPlugin;

public class CreateBizCatalog extends BizPlugin {

	@Override
	public void before(BizEvent event) {
	}

	@Override
	public void after(BizEvent event) {
		IOrm orm = event.getOrm();
		IBizCatalog catalog = (IBizCatalog) event.getEntity();

		// 将【业务分类】转换成【文件夹模块】
		Demsy.moduleEngine.makeModule(orm, Demsy.me().getSoft(), catalog);

	}

	@Override
	public void loaded(BizEvent event) {
		// TODO Auto-generated method stub

	}

}
