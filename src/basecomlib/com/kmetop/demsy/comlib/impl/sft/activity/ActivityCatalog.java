package com.kmetop.demsy.comlib.impl.sft.activity;

import static com.kmetop.demsy.biz.BizConst.*;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.base.ebusiness.product.Product;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.web.IActivity;
import com.kmetop.demsy.comlib.web.IStatistic;
import com.kmetop.demsy.lang.Str;

@Entity
@BzSys(name = "活动设置", code = "ActivityCatalog", orderby = 1//
, actions = { @BzAct(name = "添加", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
		, @BzAct(name = "统计股价竞猜", typeCode = TYPE_BZFORM_EDIT, mode = "e3", plugin = "com.kmetop.demsy.plugins.activity.StockGuessStat") //

}//
, groups = {//
@BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "type", gridField = false) //
		, @BzFld(name = "活动名称", property = "name", mode = "c:M e:M v:S e3:S *:N", gridOrder = 1)//
		, @BzFld(property = "expiredFrom", gridOrder = 2) //
		, @BzFld(property = "expiredTo", gridOrder = 3) //
		, @BzFld(property = "prizeExpiredFrom") //
		, @BzFld(property = "prizeExpiredTo") //
		, @BzFld(property = "limitLogin") //
		, @BzFld(property = "limitTimes") //
		, @BzFld(property = "votePolicy") //
		, @BzFld(property = "voteTimes") //
		, @BzFld(property = "clickNum", gridOrder = 4) //
		, @BzFld(property = "commentNum", gridOrder = 5) //
		, @BzFld(property = "attendNum", gridOrder = 6) //
}), @BzGrp(name = "图片按钮", code = "img"//
, fields = { @BzFld(property = "image") //
		, @BzFld(property = "buttonShow") //
		, @BzFld(property = "buttonDetail") //
		, @BzFld(property = "buttonEntry") //
}), @BzGrp(name = "活动详情", code = "detail"//
, fields = { @BzFld(property = "desc", name = "活动摘要", mode = "c:M e:M v:S e3:S *:N") //
		, @BzFld(property = "content", gridField = false) //
}), @BzGrp(name = "其他属性", code = "others"//
, fields = { @BzFld(property = "stockResult", gridField = false) //
		, @BzFld(property = "product", gridField = false) //
		, @BzFld(property = "productPrice", gridField = false) //
})
//
}// end groups
)
public class ActivityCatalog extends SFTBizComponent implements IActivity, IStatistic {

	@BzFld(name = "活动类型", mode = "c:M e:S *:N v:S", options = "1:摄影大赛,2:征文大赛,3:股票竞猜,4:网络任务,5:产品促销")
	protected Byte type;

	@BzFld(name = "动画图片", mode = "c:E e:E *:N v:S", uploadType = "*.bmp;*.jpg;*.gif;*.png;*.swf;*.flv")
	protected Upload image;

	@BzFld(name = "展示按钮", mode = "c:E e:E *:N v:S", uploadType = "*.bmp;*.jpg;*.gif;*.png;")
	protected Upload buttonShow;

	@BzFld(name = "详情按钮", mode = "c:E e:E *:N v:S", uploadType = "*.bmp;*.jpg;*.gif;*.png;")
	protected Upload buttonDetail;

	@BzFld(name = "投稿按钮", mode = "c:E e:E *:N v:S", uploadType = "*.bmp;*.jpg;*.gif;*.png;")
	protected Upload buttonEntry;

	@BzFld(name = "规则说明", mode = "c:E e:E *:N v:S")
	protected RichText content;

	/*
	 * 活动限制
	 */
	@BzFld(name = "活动有效期自", mode = "c:M e:M *:N v:S", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date expiredFrom;

	@BzFld(name = "活动有效期至", mode = "c:M e:M *:N v:S", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date expiredTo;

	@BzFld(name = "限制参与方式", mode = "c:E e:E *:N v:S", options = "0:匿名参与,1:实名参与", disabledNavi = true)
	protected boolean limitLogin;

	@BzFld(name = "限制参与次数", mode = "c:E e:E *:N v:S", uiTemplate = "ui.widget.field.Spinner")
	protected byte limitTimes;

	@BzFld(name = "领奖有效期自", mode = "c:E e:E *:N v:S", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date prizeExpiredFrom;

	@BzFld(name = "领奖有效期至", mode = "c:E e:E *:N v:S", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date prizeExpiredTo;

	@BzFld(name = "投票策略", mode = "c:E e:E *:N v:S", cascadeMode = "type:1,2:M", options = "0:不支持投票,1:实名投票,2:匿名投票", disabledNavi = true)
	protected byte votePolicy;

	@BzFld(name = "限制投票次数", mode = "c:E e:E *:N v:S", uiTemplate = "ui.widget.field.Spinner")
	protected byte voteTimes;

	/*
	 * 股价竞猜活动
	 */
	@BzFld(name = "收盘价", mode = "*:N v:P e3:M")
	protected Double stockResult;

	/*
	 * 产品促销
	 */
	@ManyToOne
	@BzFld(name = "促销产品", cascadeMode = "type:5:M", disabledNavi = true)
	protected Product product;

	@BzFld(name = "促销价格", cascadeMode = "type:5:M")
	protected Double productPrice;

	/*
	 * 流量统计
	 */
	@BzFld(name = "点击次数", mode = "*:N v:S")
	protected Integer clickNum;

	@BzFld(name = "评论次数", mode = "*:N v:S")
	protected Integer commentNum;

	@BzFld(name = "参与次数", mode = "*:N v:S")
	protected int attendNum;

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Upload getImage() {
		return image;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public Date getExpiredFrom() {
		return expiredFrom;
	}

	public void setExpiredFrom(Date expiredFrom) {
		this.expiredFrom = expiredFrom;
	}

	public Date getExpiredTo() {
		return expiredTo;
	}

	public void setExpiredTo(Date expiredTo) {
		this.expiredTo = expiredTo;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public int getAttendNum() {
		return attendNum;
	}

	public void setAttendNum(int attendNum) {
		this.attendNum = attendNum;
	}

	public Double getStockResult() {
		return stockResult;
	}

	public void setStockResult(Double stockResult) {
		this.stockResult = stockResult;
	}

	public boolean isLimitLogin() {
		return limitLogin;
	}

	public void setLimitLogin(boolean limitLogin) {
		this.limitLogin = limitLogin;
	}

	public byte getLimitTimes() {
		return limitTimes;
	}

	public void setLimitTimes(byte limitTimes) {
		this.limitTimes = limitTimes;
	}

	public RichText getContent() {
		if (content == null || Str.isEmpty(content.toString())) {
			return null;
		}
		return content;
	}

	public void setContent(RichText content) {
		this.content = content;
	}

	public Upload getButtonShow() {
		return buttonShow;
	}

	public void setButtonShow(Upload buttonShow) {
		this.buttonShow = buttonShow;
	}

	public Upload getButtonDetail() {
		return buttonDetail;
	}

	public void setButtonDetail(Upload buttonDetail) {
		this.buttonDetail = buttonDetail;
	}

	public Upload getButtonEntry() {
		return buttonEntry;
	}

	public void setButtonEntry(Upload buttonEntry) {
		this.buttonEntry = buttonEntry;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public Date getPrizeExpiredFrom() {
		return prizeExpiredFrom;
	}

	public void setPrizeExpiredFrom(Date prizeExpiredFrom) {
		this.prizeExpiredFrom = prizeExpiredFrom;
	}

	public Date getPrizeExpiredTo() {
		return prizeExpiredTo;
	}

	public void setPrizeExpiredTo(Date prizeExpiredTo) {
		this.prizeExpiredTo = prizeExpiredTo;
	}

	public byte getVotePolicy() {
		return votePolicy;
	}

	public void setVotePolicy(byte votePolicy) {
		this.votePolicy = votePolicy;
	}

	public byte getVoteTimes() {
		return voteTimes;
	}

	public void setVoteTimes(byte voteTimes) {
		this.voteTimes = voteTimes;
	}
}
