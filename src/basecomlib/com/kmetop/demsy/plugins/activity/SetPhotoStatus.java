package com.kmetop.demsy.plugins.activity;

import java.util.List;

import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.sft.activity.ActivityCatalog;
import com.kmetop.demsy.comlib.impl.sft.activity.PhotoActivity;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.expr.Expr;
import com.kmetop.demsy.plugins.BizPlugin;

public class SetPhotoStatus extends BizPlugin {

	@Override
	public synchronized void before(BizEvent event) {
		IOrm orm = event.getOrm();
		Object obj = event.getEntity();
		if (obj instanceof List) {
			List<PhotoActivity> list = (List) obj;
			for (PhotoActivity photo : list) {
				ActivityCatalog catalog = photo.getCatalog();
				PhotoActivity first = (PhotoActivity) orm.load(PhotoActivity.class, Expr.eq("catalog", catalog).addAsc("id"));
				long firstID = first.getId();
				photo.setCode("" + (photo.getId() - firstID + 1));
			}
		} else {
			PhotoActivity photo = (PhotoActivity) obj;
			ActivityCatalog catalog = photo.getCatalog();
			PhotoActivity first = (PhotoActivity) orm.load(PhotoActivity.class, Expr.eq("catalog", catalog).addAsc("id"));
			long firstID = first.getId();
			photo.setCode("" + (photo.getId() - firstID + 1));
		}
	}

	@Override
	public void after(BizEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loaded(BizEvent event) {
		// TODO Auto-generated method stub

	}

}
