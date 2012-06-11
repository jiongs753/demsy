package com.kmetop.demsy.comlib.impl.base.common;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_BASE;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.common.IContact;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@BzSys(name = "联系人信息", code = IContact.SYS_CODE, catalog = BIZCATA_BASE,//
actions = {
//
		@BzAct(name = "添加", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.common.SaveContact")//
		, @BzAct(name = "修改", typeCode = TYPE_BZFORM_EDIT, mode = "e", plugin = "com.kmetop.demsy.plugins.common.SaveContact") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
},//
groups = { //
@BzGrp(name = "基本信息", code = "basic",//
fields = { @BzFld(property = "createdBy", mode = "*:N v:P", name = "登录帐号", gridOrder = 1, privacy = true) //
		, @BzFld(property = "name", mode = "c:M e:M", name = "真实姓名", gridOrder = 2, privacy = true)//
		, @BzFld(property = "code", mode = "c:M e:M", name = "联系电话", gridOrder = 3, privacy = true)//
		, @BzFld(property = "province", gridOrder = 4)//
		, @BzFld(property = "city", gridOrder = 5)//
		, @BzFld(property = "area", gridOrder = 6)//
		, @BzFld(property = "desc", mode = "c:M e:M", name = "街道地址", gridOrder = 7, privacy = true) //
		, @BzFld(property = "postcode", gridOrder = 8) //
}),//
		@BzGrp(name = "其他信息", code = "other",//
		fields = { @BzFld(name = "创建时间", property = "created", mode = "*:N v:P") //
				, @BzFld(name = "修改时间", property = "created", mode = "*:N v:P") //
				, @BzFld(property = "createdIP", privacy = true) //
		}) // @BzGrp
}// end groups
)
public class Contact extends BizComponent implements IContact {
	/*
	 * 收货地址
	 */
	@Column(length = 64)
	@BzFld(name = "省", mode = "c:M e:M")
	protected String province;

	@Column(length = 64)
	@BzFld(name = "市", mode = "c:M e:M")
	protected String city;

	@Column(length = 64)
	@BzFld(name = "区", mode = "c:M e:M")
	protected String area;

	@Column(length = 15)
	@BzFld(name = "邮政编码", mode = "c:M e:M")
	protected String postcode;

	@BzFld(name = "默认地址", mode = "*:N v:P")
	protected Boolean defaults;

	@Column(length = 64)
	@BzFld(name = "IP地址", mode = "*:N v:P")
	protected String createdIP;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String createdIP) {
		this.createdIP = createdIP;
	}

	public Boolean getDefaults() {
		return defaults;
	}

	public void setDefaults(Boolean defaults) {
		this.defaults = defaults;
	}

	@Override
	public String getStreet() {
		return desc;
	}

	@Override
	public String getPerson() {
		return name;
	}

	@Override
	public String getTelcode() {
		return code;
	}

	@Override
	public void setStreet(String street) {
		this.desc = street;
	}

	@Override
	public void setPerson(String person) {
		this.name = person;
	}

	@Override
	public void setTelcode(String telcode) {
		this.code = telcode;
	}

}
