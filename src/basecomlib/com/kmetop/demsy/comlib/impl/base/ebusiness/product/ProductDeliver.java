package com.kmetop.demsy.comlib.impl.base.ebusiness.product;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.eshop.IProductDeliver;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@BzSys(name = "发货地址设置", code = IProductDeliver.SYS_CODE, orderby = 1,//
actions = {
//
		@BzAct(name = "添加", typeCode = TYPE_BZFORM_NEW, mode = "c"),//
		@BzAct(jsonData = "CommonBizAction.data.js") //
},//
groups = { @BzGrp(name = "基本信息", code = "basic",//
fields = { @BzFld(name = "名称", property = "name", mode = "c:M e:M *:N v:S")//
		, @BzFld(name = "编码", property = "code", mode = "c:M e:M *:N v:S")//
		, @BzFld(property = "address") //
		, @BzFld(property = "postcode") //
		, @BzFld(property = "corpName") //
		, @BzFld(property = "personName") //
		, @BzFld(property = "personTel") //
		, @BzFld(property = "loginuser") //
		, @BzFld(name = "人工顺序", property = "orderby", uiTemplate = "ui.widget.field.Spinner") //
}), @BzGrp(name = "物流单设置", code = "bills",//
fields = { @BzFld(property = "logisticsName") //
		, @BzFld(property = "logisticsNo") //
		, @BzFld(property = "billImage")//
		, @BzFld(name = "物流单备注", property = "desc", mode = "c:E e:E *:N v:S") //
}) }// end groups
)
public class ProductDeliver extends BizComponent implements IProductDeliver {

	@Column(length = 32)
	@BzFld(name = "单位名称", mode = "c:M e:M *:N v:S")
	protected String corpName;

	@Column(length = 32)
	@BzFld(name = "寄件人姓名", mode = "c:M e:M *:N v:S", privacy = true)
	protected String personName;

	@Column(length = 32)
	@BzFld(name = "寄件人电话", mode = "c:M e:M *:N v:S", privacy = true)
	protected String personTel;

	@Column(length = 32)
	@BzFld(name = "打单员帐号", mode = "c:M e:M *:N v:S", privacy = true)
	protected String loginuser;

	@Column(length = 256)
	@BzFld(name = "寄件地址", mode = "c:M e:M *:N v:S")
	protected String address;

	@Column(length = 15)
	@BzFld(name = "邮政编码", mode = "c:M e:M *:N v:S")
	protected String postcode;

	@BzFld(name = "物流公司", mode = "c:E e:E *:N v:S", precision = 64, options = "EMS,顺丰快递,申通E物流,圆通速递,中通速递,宅急送,韵达快运,天天快递,联邦快递,汇通快运,华强物流,快捷快递,其它", disabledNavi = true)
	protected String logisticsName;

	@Column(length = 32)
	@BzFld(name = "起始物流单", mode = "c:E e:E *:N v:S")
	protected String logisticsNo;

	@BzFld(name = "物流单图片", uploadType = "*.jpg;*.gif;*.png;*.bmp", mode = "c:E e:E *:N v:S")
	protected Upload billImage;

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonTel() {
		return personTel;
	}

	public void setPersonTel(String personTel) {
		this.personTel = personTel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getLoginuser() {
		return loginuser;
	}

	public void setLoginuser(String loginuser) {
		this.loginuser = loginuser;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public Upload getBillImage() {
		return billImage;
	}

	public void setBillImage(Upload billImage) {
		this.billImage = billImage;
	}

}
