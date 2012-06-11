package com.kmetop.demsy.plugins.biz;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.plugins.BizPlugin;

public class EditBizSystem extends BizPlugin {

	@Override
	public void before(BizEvent event) {
	}

	@Override
	public void after(BizEvent event) {
		IBizSystem sys = (IBizSystem) event.getEntity();
		if (sys == null) {
			return;
		}

		try {
			String extendClass = Demsy.bizEngine.getExtendClassName(sys);
			Demsy.bizEngine.parseSystemByAnnotation(Cls.forName(extendClass), sys);
		} catch (ClassNotFoundException e) {
			// throw new DemsyException(e);
		}
	}

	@Override
	public void loaded(BizEvent event) {
		// TODO Auto-generated method stub

	}

}
