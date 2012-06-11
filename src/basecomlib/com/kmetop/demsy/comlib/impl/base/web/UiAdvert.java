package com.kmetop.demsy.comlib.impl.base.web;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_WEB_ADVERT;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_ADVERT;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@BzSys(name = "网站广告管理", code = BIZSYS_WEB_ADVERT, catalog = BIZCATA_WEB, orderby = ORDER_WEB_ADVERT//
, actions = {
//
		@BzAct(name = "添加", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(property = "name", name = "广告名称", mode = "c:M e:M")//
		, @BzFld(property = "priority") //
		, @BzFld(property = "keywords") //
		, @BzFld(property = "clickNum") //
		, @BzFld(property = "linkNum") //
		, @BzFld(property = "linkPath") //
		, @BzFld(property = "linkTarget") //
		, @BzFld(property = "image") //
		, @BzFld(property = "disabled", name = "状态", disabledNavi = true, options = "1:停用,0:启用")//
		, @BzFld(property = "created", name = "创建时间", mode = "*:N v:S") //
		, @BzFld(property = "updated", name = "更新时间", mode = "*:N v:S") //
}) }// end groups
)
public class UiAdvert extends BizComponent {

	@Column(length = 128)
	@BzFld(name = "链接地址")
	protected String linkPath;

	@Column(length = 16)
	@BzFld(name = "链接目标", disabledNavi = true, gridField = false, options = "_blank:新窗口,_parent:父窗口")
	protected String linkTarget;

	@BzFld(name = "广告文件", uploadType = "*.bmp;*.jpg;*.gif;*.png;*.swf;*.flv", mode = "c:M e:M")
	protected Upload image;

	@BzFld(name = "优先级", disabledNavi = true, options = "1,2,3,4,5,6,7,8,9")
	protected Integer priority;

	@Column(length = 256)
	@BzFld(name = "关键字")
	protected String keywords;

	@BzFld(name = "查看次数", mode = "*:N v:S")
	protected Integer clickNum;

	@BzFld(name = "链接次数", mode = "*:N v:S")
	protected Integer linkNum;

	public String getLinkPath() {
		return linkPath;
	}

	public String getLinkTarget() {
		return linkTarget;
	}

	public Integer getPriority() {
		return priority;
	}

	public String getKeywords() {
		return keywords;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public void setLinkPath(String linkPath) {
		this.linkPath = linkPath;
	}

	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	public Upload getImage() {
		return image;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public Integer getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(Integer linkNum) {
		this.linkNum = linkNum;
	}

}
