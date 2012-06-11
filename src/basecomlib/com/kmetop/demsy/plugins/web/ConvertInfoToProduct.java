package com.kmetop.demsy.plugins.web;

import java.util.List;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.base.ebusiness.product.Product;
import com.kmetop.demsy.comlib.impl.sft.web.content.WebContent;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.plugins.BizPlugin;

public class ConvertInfoToProduct extends BizPlugin {

	@Override
	public void before(BizEvent event) {
	}

	@Override
	public void after(BizEvent event) {
	}

	@Override
	public void loaded(BizEvent event) {
		IOrm orm = Demsy.orm();

		List<WebContent> list = (List<WebContent>) event.getReturnValue();
		for (WebContent info : list) {
			Product p = (Product) orm.load(Product.class, Expr.eq("name", info.getName()));
			if (p == null) {
				// p = new Product();
				// p.setImage(info.getImage());
				// p.setContent(info.getContent());
				// p.setName(info.getName());
				continue;
			}

			p.setClickNum(info.getClickNum());

			orm.save(p);
		}
	}

}
