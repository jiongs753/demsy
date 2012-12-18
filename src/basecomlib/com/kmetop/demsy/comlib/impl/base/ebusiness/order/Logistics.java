package com.kmetop.demsy.comlib.impl.base.ebusiness.order;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EXPORT_XLS;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_PRINT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.eshop.ILogistics;
import com.kmetop.demsy.comlib.eshop.IProductDeliver;
import com.kmetop.demsy.comlib.eshop.IProductOperator;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.impl.base.ebusiness.product.ProductDeliver;
import com.kmetop.demsy.comlib.impl.base.ebusiness.product.ProductOperator;
import com.kmetop.demsy.lang.Str;

@Entity
@BzSys(name = "订单发货管理", code = ILogistics.SYS_CODE, orderby = 3,//
actions = {
		@BzAct(name = "发货说明", typeCode = TYPE_BZFORM_EDIT, mode = "e2")//
		, @BzAct(name = "发货打印", typeCode = TYPE_BZFORM_PRINT, mode = "p", template = "ui.print.LogisticsBill", params = "printNum eq 0", info = "订单发货成功", error = "订单发货失败", plugin = "com.kmetop.demsy.plugins.eshop.PrintLogisticsBill") //
		, @BzAct(name = "订单详情", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
		, @BzAct(name = "转发", typeCode = TYPE_BZFORM_EDIT, mode = "e1")//
		, @BzAct(name = "导出XLS", typeCode = TYPE_BZFORM_EXPORT_XLS, mode = "xls") //
		, @BzAct(name = "永久删除", typeCode = TYPE_BZ_DEL, mode = "d") //
},//
groups = { //
@BzGrp(name = "基本信息", code = "basic",//
fields = { @BzFld(property = "orderID", gridOrder = 1)//
		, @BzFld(property = "orderDate", gridOrder = 6) //
		, @BzFld(property = "deliver")//
		, @BzFld(property = "operator")//
		, @BzFld(property = "subject", name = "邮寄地址", precision = 512, isTransient = true, mode = "*:N v:S", gridOrder = 2, privacy = true)//
		, @BzFld(property = "itemsCatalog")//
		, @BzFld(property = "itemsAmount")//
		, @BzFld(property = "totalCost", gridOrder = 4)//
		, @BzFld(property = "sendGoodsInfo", name = "快递信息", isTransient = true, mode = "*:N v:S", gridOrder = 5)//
		, @BzFld(property = "code", name = "快递单号", mode = "*:N v:S p:M")//
		, @BzFld(property = "printNum")//
		, @BzFld(property = "printDate") //
		, @BzFld(property = "logisticsName")//
		, @BzFld(property = "address")//
		, @BzFld(property = "postcode")//
		, @BzFld(property = "name", name = "收件人", mode = "*:N v:S", privacy = true)//
		, @BzFld(property = "telcode", privacy = true) //
		, @BzFld(property = "desc", mode = "*:N v:S", name = "商品清单", gridOrder = 3) //
		, @BzFld(property = "note", gridOrder = 7) //
		, @BzFld(property = "note2", gridOrder = 8) //
}) }// end groups
)
public class Logistics extends BizComponent implements ILogistics {
	// 将订单按发货地址拆分成物流单
	@ManyToOne
	@BzFld(name = "发货地址", mode = "*:N v:S e1:M")
	protected ProductDeliver deliver;

	// 将订单按运营上拆分成物流单
	@ManyToOne
	@BzFld(name = "运营机构", mode = "*:N v:S e1:M")
	protected ProductOperator operator;

	@OneToMany(mappedBy = "logistics")
	protected List<LogisticsItem> items;

	@BzFld(name = "物流公司", precision = 64, mode = "*:N v:S p:M")
	protected String logisticsName;

	@Column(length = 32)
	@BzFld(name = "订单号", mode = "*:N v:S", gridWidth = 100)
	protected String orderID;

	@Column(length = 256)
	@BzFld(name = "收货地址", mode = "*:N v:S", privacy = true)
	protected String address;

	@Column(length = 15)
	@BzFld(name = "邮政编码", mode = "*:N v:S")
	protected String postcode;

	@Column(length = 255)
	@BzFld(name = "联系电话", mode = "*:N v:S", privacy = true)
	protected String telcode;

	@BzFld(name = "清单条目", mode = "*:N v:S")
	protected int itemsCatalog;

	@BzFld(name = "商品数量", mode = "*:N v:S")
	protected int itemsAmount;

	@BzFld(name = "合计(元)", mode = "*:N v:S", pattern = "#,##0.00")
	protected Double totalCost;

	@BzFld(name = "打印次数", mode = "*:N v:S")
	protected int printNum;

	@BzFld(name = "订单时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm")
	protected Date orderDate;

	@BzFld(name = "发货时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm")
	protected Date printDate;

	@BzFld(name = "订单留言", mode = "*:N e2:S v:S")
	protected String note;
	
	@BzFld(name = "发货说明", mode = "*:N e2:E v:S")
	protected RichText note2;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSendGoodsInfo() {
		if (!Str.isEmpty(code))
			return new StringBuffer().append(logisticsName).append("(").append(code).append(")").toString();

		return "";
	}

	public String getSubject() {
		return new StringBuffer().append(address).append("(").append(name).append(" ").append(telcode).append(")").toString();
	}

	@Override
	public void setPersonName(String name) {
		super.setName(name);
	}

	@Override
	public String getPersonName() {
		return super.getName();
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
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

	public String getTelcode() {
		return telcode;
	}

	public void setTelcode(String telcode) {
		this.telcode = telcode;
	}

	public int getItemsCatalog() {
		return itemsCatalog;
	}

	public void setItemsCatalog(int itemsCatalog) {
		this.itemsCatalog = itemsCatalog;
	}

	public int getItemsAmount() {
		return itemsAmount;
	}

	public void setItemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
	}

	public int getPrintNum() {
		return printNum;
	}

	public void setPrintNum(int printTimes) {
		this.printNum = printTimes;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public Date getCurrentDate() {
		return new Date();
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public String toString() {
		String code = getCode();
		return this.getOrderID() + (code == null ? "" : ("(" + code + ")"));
	}

	public ProductDeliver getDeliver() {
		return deliver;
	}

	public void setDeliver(IProductDeliver storage) {
		this.deliver = (ProductDeliver) storage;
	}

	public List<LogisticsItem> getItems() {
		return items;
	}

	public void setItems(List<LogisticsItem> items) {
		this.items = items;
	}

	public void setDeliver(ProductDeliver deliver) {
		this.deliver = deliver;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public ProductOperator getOperator() {
		return operator;
	}

	public void setOperator(IProductOperator operator) {
		this.operator = (ProductOperator) operator;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public RichText getNote2() {
		return note2;
	}

	public void setNote2(RichText sendGoodsNote) {
		this.note2 = sendGoodsNote;
	}
}
