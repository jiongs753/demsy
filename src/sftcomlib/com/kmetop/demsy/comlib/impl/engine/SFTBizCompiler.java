package com.kmetop.demsy.comlib.impl.engine;

import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.engine.BizCompiler;
import com.kmetop.demsy.engine.BizEngine;

public class SFTBizCompiler extends BizCompiler {

	SFTBizCompiler(BizEngine engine) {
		super(engine);
	}

	@Override
	protected String genSrcOfImport(IBizSystem system) {
		return new StringBuffer().append("\n")//
				.append("\nimport java.util.*;")//
				.append("\nimport java.math.*;")//
				.append("\nimport javax.persistence.*;")//
				.append("\nimport com.kmetop.demsy.comlib.biz.ann.*;")//
				.append("\nimport com.kmetop.demsy.comlib.biz.field.*;")//
				.append("\nimport com.kmetop.demsy.comlib.impl.sft.*;")//
				.append("\nimport com.kmetop.demsy.comlib.impl.sft.dic.*;")//
				.append("\nimport com.kmetop.demsy.comlib.entity.base.*;")//
				.toString();
	}

}
