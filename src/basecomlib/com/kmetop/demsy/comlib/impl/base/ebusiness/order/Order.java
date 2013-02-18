package com.kmetop.demsy.comlib.impl.base.ebusiness.order;

import static com.kmetop.demsy.biz.BizConst.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.eshop.IOrder;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.lang.Dates;
import com.kmetop.demsy.lang.Str;

@Entity
@BzSys(name = "订单信息管理", code = IOrder.SYS_CODE, orderby = 1,//
actions = { @BzAct(name = "付款", typeCode = TYPE_BZFORM_EDIT, mode = "e2", plugin = "com.kmetop.demsy.plugins.eshop.OrderBuyerPayed")//
		, @BzAct(name = "退款", typeCode = TYPE_BZFORM_EDIT, mode = "e3", plugin = "com.kmetop.demsy.plugins.eshop.OrderRefund")//
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d", plugin = "com.kmetop.demsy.plugins.eshop.OrderDelete")//
		// , @BzAct(name = "发货处理", typeCode = TYPE_BZ_EDIT, mode = "e1", plugin
		// = "com.kmetop.demsy.plugins.eshop.OrderSended")//
		// , @BzAct(name = "调整费用", typeCode = TYPE_BZ_EDIT_N, mode = "bu",
		// plugin = "com.kmetop.demsy.plugins.eshop.OrderAdjustCast")//
		, @BzAct(name = "详情", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
		, @BzAct(name = "导出到XLS", typeCode = TYPE_BZFORM_EXPORT_XLS, mode = "xls") //
// , @BzAct(name = "永久删除", typeCode = TYPE_BZ_DEL, mode = "d") //
},//
groups = { //
@BzGrp(name = "基本信息", code = "basic",//
fields = { @BzFld(property = "timeID")//
		, @BzFld(property = "created", name = "下单时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm") //
		, @BzFld(property = "name", name = "收件人", mode = "*:N v:S", privacy = true)//
		, @BzFld(property = "code", name = "联系电话", mode = "*:N v:S", privacy = true)//
		, @BzFld(property = "postcode")//
		, @BzFld(property = "address")//
		, @BzFld(property = "itemsCatalog") //
		, @BzFld(property = "itemsAmount", gridOrder = 3) //
		, @BzFld(property = "itemsCost") //
		// , @BzFld(property = "discount") //
		, @BzFld(property = "logisticsCost") //
		, @BzFld(property = "totalCost", gridOrder = 4) //
		, @BzFld(property = "createdBy", name = "登录帐号", mode = "*:N v:S") //
		, @BzFld(property = "createdIP", privacy = true) //
		, @BzFld(property = "orderInfo", name = "订单信息", isTransient = true, mode = "*:N v:S e1:S e2:S", gridOrder = 1)//
		, @BzFld(property = "buyerInfo", name = "买家信息", isTransient = true, mode = "*:N v:S e1:S e2:S", gridOrder = 5, privacy = true)//
		, @BzFld(property = "payInfo", name = "支付信息", isTransient = true, mode = "*:N v:S e1:S e2:S", gridOrder = 6, privacy = true)//
		, @BzFld(property = "webUserInfo", name = "会员信息", isTransient = true, mode = "*:N v:S e1:S e2:S", privacy = true) //
		, @BzFld(property = "desc", mode = "*:N v:S", name = "清单信息") //
		, @BzFld(property = "note", gridOrder = 8) //
}), @BzGrp(name = "支付物流", code = "trade",//
fields = { @BzFld(property = "paytype") //
		, @BzFld(property = "tradeID") //
		, @BzFld(property = "payTime") //
		, @BzFld(property = "logisticsType") //
		, @BzFld(property = "logisticsName") //
		, @BzFld(property = "logisticsNum") //
		, @BzFld(property = "logisticsID") //
		, @BzFld(property = "logisticsTime") //
		, @BzFld(property = "logisticsNote", gridOrder = 7) //
}), @BzGrp(name = "订单状态", code = "other",//
fields = { @BzFld(property = "type") //
		, @BzFld(property = "status", gridOrder = 2) //
		, @BzFld(property = "activityID") //
		, @BzFld(property = "activityComment") //
}) }// end groups
)
public class Order extends BizComponent implements IOrder {
	public static Map<String, String> paytypeMap = new HashMap();
	static {
		paytypeMap.put("ICBCB2C", "中国工商银行");
		paytypeMap.put("CMB", "招商银行");
		paytypeMap.put("CCB", "中国建设银行");
		paytypeMap.put("BOCB2C", "中国银行");
		paytypeMap.put("ABC", "中国农业银行");
		paytypeMap.put("COMM", "交通银行");
		paytypeMap.put("SPDB", "浦发银行");
		paytypeMap.put("GDB", "广发银行");
		paytypeMap.put("CITIC", "中信银行");
		paytypeMap.put("CEBBANK", "光大银行");
		paytypeMap.put("CIB", "兴业银行");
		paytypeMap.put("SDB", "深圳发展银行");
		paytypeMap.put("CMBC", "民生银行");
		paytypeMap.put("HZCBB2C", "杭州银行");
		paytypeMap.put("SHBANK", "上海银行");
		paytypeMap.put("NBBANK", "宁波银行");
		paytypeMap.put("SPABANK", "平安银行");
		paytypeMap.put("BJRCB", "北京农商银行");
		paytypeMap.put("ICBCBTB", "中国工商银行(企业)");
		paytypeMap.put("CCBBTB", "中国建设银行(企业)");
		paytypeMap.put("SPDBB2B", "浦发银行(企业)");
		paytypeMap.put("ABCBTB", "中国农业银行(企业)");
		paytypeMap.put("fdb101", "富滇银行");
		paytypeMap.put("PSBC-DEBIT", "中国邮政储蓄");
		paytypeMap.put("0", "支付宝担保交易");
		paytypeMap.put("1", "支付宝即时到帐");
	}

	@Column(length = 32)
	@BzFld(name = "订单号", mode = "*:N v:S e1:S e2:S e3:S")
	protected String timeID;

	@Column(length = 255)
	@BzFld(name = "收货地址", mode = "*:N v:S e1:S e2:S e3:S", privacy = true)
	protected String address;

	@Column(length = 15)
	@BzFld(name = "邮政编码", mode = "*:N v:S e1:S e2:S e3:S")
	protected String postcode;

	/*
	 * 订单支付
	 */
	@BzFld(name = "物流费用(元)", mode = "*:N v:S bu:E e3:S", pattern = "#,##0.00")
	protected Double logisticsCost;

	@BzFld(name = "商品费用(元)", mode = "*:N v:S e1:S e2:S e3:S", pattern = "#,##0.00")
	protected Double itemsCost;

	@BzFld(name = "清单数目", mode = "*:N v:S e1:S e2:S e3:S")
	protected int itemsCatalog;

	@BzFld(name = "商品数量", mode = "*:N v:S e1:S e2:S e3:S")
	protected int itemsAmount;

	//
	// @BzFld(name = "折扣", mode = "*:N v:S bu:E")
	// protected Double discount;

	@BzFld(name = "合计(元)", mode = "*:N v:S e1:S e2:S e3:S", pattern = "#,##0.00")
	protected Double totalCost;

	/*
	 * 订单状态
	 */
	@BzFld(name = "订单状态", mode = "*:N v:S e1:S e2:S e3:S", options = "0:未付款,1:已付款(未发货),2:已发货,3:交易成功,4:申请退款,5:已退款,9:交易结束,19:交易关闭")
	protected byte status;

	@BzFld(name = "订单类型", mode = "*:N v:S e1:S e2:S e3:S", options = "1:购物车订单,2:立即购买订单,11:团购订单,21:秒杀订单")
	protected byte type;

	@Column(length = 64)
	@BzFld(name = "IP地址", mode = "*:N v:S e1:S e2:S e3:S")
	protected String createdIP;

	/**
	 * 交易方式
	 * <p>
	 * ICBCB2C:中国工商银行,CMB:招商银行,CCB:中国建设银行,BOCB2C:中国银行
	 * <p>
	 * ,ABC:中国农业银行,COMM:交通银行,SPDB:浦发银行,GDB:广发银行
	 * <p>
	 * ,CITIC:中信银行,CEBBANK:光大银行,CIB:兴业银行,SDB:深圳发展银行
	 * <p>
	 * ,CMBC:民生银行,HZCBB2C:杭州银行,SHBANK:上海银行,NBBANK:宁波银行
	 * <p>
	 * ,SPABANK:平安银行,BJRCB:北京农商银行,ICBCBTB:中国工商银行(企业),CCBBTB:中国建设银行(企业)
	 * <p>
	 * ,SPDBB2B:浦发银行(企业),ABCBTB:中国农业银行(企业),fdb101:富滇银行,PSBC-DEBIT:中国邮政储蓄
	 * <p>
	 * ,0:支付宝担保交易,1:支付宝即时到帐
	 */
	@Column(length = 16)
	@BzFld(name = "支付方式", mode = "*:N v:S e2:M e3:S", options = "ICBCB2C:中国工商银行,CMB:招商银行,CCB:中国建设银行,BOCB2C:中国银行,ABC:中国农业银行,COMM:交通银行,SPDB:浦发银行,GDB:广发银行,CITIC:中信银行,CEBBANK:光大银行,CIB:兴业银行,SDB:深圳发展银行,CMBC:民生银行,HZCBB2C:杭州银行,SHBANK:上海银行,NBBANK:宁波银行,SPABANK:平安银行,BJRCB:北京农商银行,ICBCBTB:中国工商银行(企业),CCBBTB:中国建设银行(企业),SPDBB2B:浦发银行(企业),ABCBTB:中国农业银行(企业),fdb101:富滇银行,PSBC-DEBIT:中国邮政储蓄,0:支付宝担保交易,1:支付宝即时到帐", disabledNavi = true)
	protected String paytype;

	@BzFld(name = "支付交易号", mode = "*:N v:S e2:M e3:S", precision = 64, privacy = true)
	protected String tradeID;

	@BzFld(name = "付款时间", mode = "*:N v:S e2:M e3:S", pattern = "yyyy-MM-dd HH:mm")
	protected Date payTime;

	@BzFld(name = "物流方式", precision = 64, mode = "*:N v:S e1:M e3:S", options = "2:快递,1:平邮,8:不需要物流", disabledNavi = true)
	protected Integer logisticsType = 2;

	@BzFld(name = "物流公司", precision = 256, cascadeMode = "logisticsType:2:M,logisticsType:1,8:N", mode = "*:N v:S e1:M e3:S", disabledNavi = true)
	protected String logisticsName;

	@BzFld(name = "物流单号", precision = 256, mode = "*:N v:S e1:M e3:S", cascadeMode = "logisticsType:1,2:M,logisticsType:8:N")
	protected String logisticsID;

	@BzFld(name = "物流备注", precision = 256, mode = "*:N v:S e1:E e3:S")
	protected String logisticsNote;

	@BzFld(name = "发货时间", mode = "*:N v:S e3:S", pattern = "yyyy-MM-dd HH:mm")
	protected Date logisticsTime;

	@BzFld(name = "物流单数", mode = "*:N v:S e3:S")
	protected int logisticsNum;

	@BzFld(name = "活动ID", mode = "*:N v:S")
	protected String activityID;

	@BzFld(name = "活动备注", mode = "*:N v:S")
	protected String activityComment;

	@BzFld(name = "订单留言", mode = "*:N v:S e1:S e2:S e3:S")
	protected String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBuyerInfo() {
		return new StringBuffer().append(address).append("(").append(name).append(" ").append(code).append(")").toString();
	}

	public String getBuyerHideInfo() {
		StringBuffer sb = new StringBuffer();

		sb.append(Str.secretStr(address));
		sb.append("(");
		sb.append(Str.secretStr(name)).append(" ").append(Str.secretStr(code));
		sb.append(")");

		return sb.toString();
	}

	public String getPayInfo() {
		if (paytype == null)
			paytype = "";

		if (!Str.isEmpty(tradeID))
			return new StringBuffer().append(paytypeMap.get(paytype.trim())).append("(").append(tradeID).append(")").toString();
		else if (!Str.isEmpty(paytype))
			return paytypeMap.get(paytype.trim());

		return "";
	}

	public String getOrderInfo() {
		return new StringBuffer().append(getOrderID()).append("(").append(Dates.formatDate(this.created, "MM月dd日HH:mm")).append(")").toString();
	}

	public String getLogisticsInfo() {
		if (!Str.isEmpty(logisticsID))
			return new StringBuffer().append(this.logisticsName).append("(").append(logisticsID).append(")").toString();
		return "";
	}

	public String getWebUserInfo() {
		StringBuffer sb = new StringBuffer(Str.ipToName(createdIP));
		if (!Str.isEmpty(createdBy)) {
			sb.append("(").append(createdBy).append(")");
		}
		return sb.toString();
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String payType) {
		this.paytype = payType;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	// public Double getDiscount() {
	// return discount;
	// }
	//
	// public void setDiscount(Double discount) {
	// this.discount = discount;
	// }

	public Double getLogisticsCost() {
		return logisticsCost;
	}

	public void setLogisticsCost(Double postFee) {
		this.logisticsCost = postFee;
	}

	public Double getItemsCost() {
		return itemsCost;
	}

	public void setItemsCost(Double shouldFee) {
		this.itemsCost = shouldFee;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double realFee) {
		this.totalCost = realFee;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String createdIP) {
		this.createdIP = createdIP;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
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

	public String toString() {
		return getTimeID();
	}

	public String getTradeID() {
		return tradeID;
	}

	public void setTradeID(String tradeID) {
		this.tradeID = tradeID;
	}

	public Integer getLogisticsType() {
		return logisticsType;
	}

	public void setLogisticsType(Integer logisticsType) {
		this.logisticsType = logisticsType;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getLogisticsID() {
		return logisticsID;
	}

	public void setLogisticsID(String logisticsID) {
		this.logisticsID = logisticsID;
	}

	public String getLogisticsNote() {
		return logisticsNote;
	}

	public void setLogisticsNote(String logisticsNote) {
		this.logisticsNote = logisticsNote;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getLogisticsTime() {
		return logisticsTime;
	}

	public void setLogisticsTime(Date logisticsTime) {
		this.logisticsTime = logisticsTime;
	}

	public String getTimeID() {
		return timeID;
	}

	public void setTimeID(String timeID) {
		this.timeID = timeID;
	}

	@Override
	public void setTelcode(String code) {
		super.setCode(code);
	}

	@Override
	public String getTelcode() {
		return getCode();
	}

	@Override
	public void setPersonName(String name) {
		super.setName(name);
	}

	@Override
	public String getPersonName() {
		return super.getName();
	}

	@Override
	public String getOrderID() {
		return this.getTimeID();
	}

	@Override
	public void setOrderID(String orderID) {
		this.setTimeID(orderID);
	}

	public int getLogisticsNum() {
		return logisticsNum;
	}

	public void setLogisticsNum(int logisticsNum) {
		this.logisticsNum = logisticsNum;
	}

	public String getActivityID() {
		return activityID;
	}

	public void setActivityID(String activityID) {
		this.activityID = activityID;
	}

	public String getActivityComment() {
		return activityComment;
	}

	public void setActivityComment(String activityComment) {
		this.activityComment = activityComment;
	}
}
