package com.kmetop.demsy.plugins.common;

import com.kmetop.demsy.biz.BizEvent;
import com.kmetop.demsy.comlib.impl.base.common.Contact;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.plugins.BizPlugin;

public class SaveContact extends BizPlugin {

	@Override
	public void before(BizEvent event) {
		StringBuffer error = new StringBuffer();
		Contact c = (Contact) event.getEntity();
		if (Str.isEmpty(c.getProvince()))
			error.append("省份必填；");
		if (Str.isEmpty(c.getCity()))
			error.append("城市必填；");
//		if (Str.isEmpty(c.getArea()))
//			error.append("区县必填；");

		if (!Str.hasChinese(c.getStreet()))
			error.append("街道名称必须包含汉字且长度不得小于4；");
		else if (c.getStreet().length() < 4)
			error.append("街道名称必须包含汉字且长度不得小于4；");

		if (!Str.isPostcode(c.getPostcode()))
			error.append("邮政编码必须是6位数字；");
		if (!Str.isRealname(c.getPerson()))
			error.append("收件人姓名必须是2个以上的汉字；");
		if (!Str.isTelcode(c.getTelcode()))
			error.append("电话号码必须是11位数字，固定电话请加上区号；");

		if (error.length() > 0) {
			throw new DemsyException(error.toString());
		}
	}

	@Override
	public void after(BizEvent event) {

	}

	@Override
	public void loaded(BizEvent event) {

	}

}
