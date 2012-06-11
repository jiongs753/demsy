package com.kmetop.demsy.comlib.impl.base.ebusiness.order;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.eshop.ILogistics;
import com.kmetop.demsy.comlib.eshop.ILogisticsItem;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@BzSys(name = "发货清单", code = ILogisticsItem.SYS_CODE, orderby = 4,//
actions = { @BzAct(name = "详情", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
// , @BzAct(name = "永久删除", typeCode = TYPE_BZ_DEL, mode = "d") //
},//
groups = { //
@BzGrp(name = "基本信息", code = "basic",//
fields = { @BzFld(property = "logistics", gridOrder = 1) //
		, @BzFld(name = "商品名称", property = "name", mode = "*:N v:S", gridOrder = 2)//
		, @BzFld(name = "商品编码", property = "code", mode = "*:N v:S", gridOrder = 3)//
		, @BzFld(property = "price") //
		, @BzFld(property = "amount") //
		, @BzFld(property = "subtotal") //
		, @BzFld(property = "orderID") //
		, @BzFld(property = "orderItemID") //
		, @BzFld(property = "productID") //
}) })
public class LogisticsItem extends BizComponent implements ILogisticsItem {
	@ManyToOne
	@BzFld(name = "物流单", mode = "*:N v:S", disabledNavi = true, masterMapping = true)
	protected Logistics logistics;

	@BzFld(name = "单价(元)", mode = "*:N v:S", pattern = "#,##0.00")
	protected Double price;

	@BzFld(name = "数量", mode = "*:N v:S")
	protected int amount;

	@BzFld(name = "小计(元)", mode = "*:N v:S", pattern = "#,##0.00")
	protected Double subtotal;

	@Column(length = 32)
	@BzFld(name = "订单号", mode = "*:N v:S")
	protected String orderID;

	@BzFld(name = "订单条目ID", mode = "*:N v:S")
	protected Long orderItemID;

	@BzFld(name = "产品ID", mode = "*:N v:S")
	protected Long productID;

	public Logistics getLogistics() {
		return logistics;
	}

	public void setLogistics(ILogistics logistics) {
		this.logistics = (Logistics) logistics;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Long getOrderItemID() {
		return orderItemID;
	}

	public void setOrderItemID(Long orderItemID) {
		this.orderItemID = orderItemID;
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public void setLogistics(Logistics logistics) {
		this.logistics = logistics;
	}
}
