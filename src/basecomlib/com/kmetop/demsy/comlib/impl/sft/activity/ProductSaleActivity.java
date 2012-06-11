package com.kmetop.demsy.comlib.impl.sft.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
@BzSys(name = "产品促销", code = "ProductSaleActivity", orderby = 4//
, actions = { @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "catalog", gridOrder = 1) //
		, @BzFld(property = "buyerInfo", gridOrder = 2) //
		, @BzFld(property = "orderInfo", gridOrder = 3) //
		, @BzFld(property = "tradeInfo", gridOrder = 4) //
		, @BzFld(property = "createdIP", gridOrder = 5) //
}) //
}// end groups
)
public class ProductSaleActivity extends SFTBizComponent {
	@BzFld(name = "活动分类", mode = "c:HM e:M", options = "['type eq 5']")
	@ManyToOne
	protected ActivityCatalog catalog;

	@BzFld(name = "买家信息", mode = "c:M *:S", privacy = true)
	protected String buyerInfo;

	@BzFld(name = "订单信息", mode = "c:M *:S", privacy = true)
	protected String orderInfo;

	@BzFld(name = "交易信息", mode = "c:M *:S", privacy = true)
	protected String tradeInfo;

	@Column(length = 32)
	@BzFld(name = "IP地址", mode = "*:N v:S", privacy = true)
	protected String createdIP;

	public ActivityCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(ActivityCatalog catalog) {
		this.catalog = catalog;
	}

	public String getBuyerInfo() {
		return buyerInfo;
	}

	public void setBuyerInfo(String buyerInfo) {
		this.buyerInfo = buyerInfo;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getTradeInfo() {
		return tradeInfo;
	}

	public void setTradeInfo(String tradeInfo) {
		this.tradeInfo = tradeInfo;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String createdIP) {
		this.createdIP = createdIP;
	}
}
