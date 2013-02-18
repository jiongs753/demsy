package com.kmetop.demsy.comlib.impl.sft.activity;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_EXEC_SYNC;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.web.IActivityEntry;

@Entity
@BzSys(name = "股价竞猜", code = "StockActivity", orderby = 3//
, actions = { @BzAct(name = "竞猜", typeCode = TYPE_BZFORM_NEW, mode = "c", info = "竞猜成功！", plugin = "com.kmetop.demsy.plugins.activity.SaveActivityEntry")//
		, @BzAct(name = "详情", typeCode = TYPE_BZFORM_EDIT, mode = "v")//
		, @BzAct(name = "兑奖", typeCode = TYPE_BZ_EXEC_SYNC, mode = "bu1", plugin = "com.kmetop.demsy.plugins.activity.StockGuessPrize") //
		, @BzAct(name = "领奖", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu") //
}//
, groups = { @BzGrp(name = "其他信息", code = "basic"//
, fields = { @BzFld(property = "catalog") //
		, @BzFld(property = "catalog.name", isTransient = true, mode = "*:N v:S", name = "活动名称") //
		, @BzFld(property = "created", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm:ss", name = "竞猜时间") //
		, @BzFld(property = "createdBy", mode = "*:N v:S", name = "竞猜帐号") //
		, @BzFld(property = "createdIP") //
}), @BzGrp(name = "竞猜信息", code = "contact"//
, fields = { @BzFld(property = "guessValue", gridOrder = 4) //
		, @BzFld(property = "username", gridOrder = 1) //
		, @BzFld(property = "sex", privacy = true) //
		, @BzFld(property = "tel", privacy = true, gridOrder = 2) //
		, @BzFld(property = "postcode") //
		, @BzFld(property = "address", gridOrder = 3) //
}), @BzGrp(name = "兑奖信息", code = "prize"//
, fields = { @BzFld(property = "resultOrder", gridOrder = 5) //
		, @BzFld(property = "guessOffset") //
		, @BzFld(property = "prizeOrder", gridOrder = 6) //
		, @BzFld(property = "prizeDate", gridOrder = 7) //
		, @BzFld(property = "status", gridOrder = 8) //

}) //
}// end groups
)
public class StockActivity extends SFTBizComponent implements IActivityEntry {
	/**
	 * 未兑奖（已排名）
	 */
	public static final byte STATUS_UNPRIZED = 1;

	/**
	 * 已兑奖（订单兑奖）
	 */
	public static final byte STATUS_PRIZED_ORDER = 2;

	/**
	 * 已领奖（奖品已发放）
	 */
	public static final byte STATUS_SENDED_GOODS = 9;

	/**
	 * 无效
	 */
	public static final byte STATUS_CANCEL = 99;

	@BzFld(name = "活动分类", mode = "c:HM *:N", options = "['type eq 3']")
	@ManyToOne
	protected ActivityCatalog catalog;

	@BzFld(name = "竞猜股价", mode = "c:M v:S *:N", pattern = "##.00")
	protected Double guessValue;

	@Column(length = 32)
	@BzFld(name = "IP地址", mode = "*:N v:S", privacy = true)
	protected String createdIP;

	@Column(length = 32)
	@BzFld(name = "用户类型", mode = "*:N v:S")
	protected String usertype;

	/*
	 * 联系方式
	 */
	@BzFld(name = "真实姓名", mode = "c:M v:S *:N", privacy = true)
	private String username;

	@BzFld(name = "性别", mode = "c:M v:S *:N", options = "1:男,0:女", disabledNavi = true)
	private Byte sex;

	@Column(length = 32)
	@BzFld(name = "手机号码", mode = "c:M v:S *:N", privacy = true)
	private String tel;

	@Column(length = 32)
	@BzFld(name = "QQ号码", mode = "c:M v:S *:N", privacy = true)
	private String qq;

	@Column(length = 12)
	@BzFld(name = "邮政编码", mode = "c:M v:S *:N", privacy = true)
	private String postcode;

	@Column(length = 256)
	@BzFld(name = "通讯地址", mode = "c:M v:S *:N", privacy = true)
	private String address;

	// 统计
	@BzFld(name = "竞猜排名", mode = "c:N v:S *:N")
	protected Integer resultOrder;

	@BzFld(name = "竞猜状态", mode = "bu:M c:N v:S *:N", options = "0:新建,1:未兑奖(已排名),2:已兑奖(订单兑奖),9:已领奖(奖品已发放),99:无效")
	private Byte status;

	@BzFld(name = "竞猜偏差", mode = "c:N v:S *:N")
	protected Double guessOffset;

	@BzFld(name = "兑奖时间", mode = "c:N v:S *:N", pattern = "YYYY-MM-dd HH:mm:ss")
	private Date prizeDate;

	@BzFld(name = "兑奖订单", mode = "c:N v:S *:N")
	private String prizeOrder;

	public ActivityCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(ActivityCatalog catalog) {
		this.catalog = catalog;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String ip) {
		this.createdIP = ip;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Byte getSex() {
		return sex;
	}

	public void setSex(Byte sex) {
		this.sex = sex;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Double getGuessValue() {
		return guessValue;
	}

	public void setGuessValue(Double guessValue) {
		this.guessValue = guessValue;
	}

	public Integer getResultOrder() {
		return resultOrder;
	}

	public void setResultOrder(Integer resultOrder) {
		this.resultOrder = resultOrder;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getPrizeOrder() {
		return prizeOrder;
	}

	public void setPrizeOrder(String prizeOrderID) {
		this.prizeOrder = prizeOrderID;
	}

	public Date getPrizeDate() {
		return prizeDate;
	}

	public void setPrizeDate(Date prizeDate) {
		this.prizeDate = prizeDate;
	}

	public Double getGuessOffset() {
		return guessOffset;
	}

	public void setGuessOffset(Double guessOffset) {
		this.guessOffset = guessOffset;
	}
}
