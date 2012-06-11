package com.kmetop.demsy.comlib.impl.sft.activity;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.web.IActivityEntry;

@Entity
@BzSys(name = "摄影投稿", code = "PhotoActivity", orderby = 2//
, actions = { @BzAct(name = "投稿", typeCode = TYPE_BZFORM_NEW, mode = "c", info = "投稿成功，审核后发布！", plugin = "com.kmetop.demsy.plugins.activity.SaveActivityEntry")//
		, @BzAct(name = "审核", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu", plugin = "com.kmetop.demsy.plugins.activity.SetPhotoStatus")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "作品信息", code = "basic"//
, fields = { @BzFld(name = "作品标题", property = "name", mode = "c:M e:M *:N v:S", gridOrder = 3)//
		, @BzFld(name = "作品编号", property = "code", mode = "*:N v:S", gridOrder = 4)//
		, @BzFld(property = "catalog", gridOrder = 7) //
		, @BzFld(property = "image") //
		, @BzFld(name = "投稿时间", property = "created", mode = "*:N v:S", gridOrder = 5, pattern = "yyyy-MM-dd HH:mm") //
		, @BzFld(name = "作者帐号", property = "createdBy", mode = "*:N v:S") //
		, @BzFld(name = "作品描述", property = "desc", mode = "c:M e:M *:N v:S") //
		, @BzFld(property = "createdIP", gridOrder = 6) //
		, @BzFld(property = "commentNum") //
		, @BzFld(property = "clickNum") //
		, @BzFld(property = "voteAgreeNum") //
		, @BzFld(property = "voteOpposeNum") //
		, @BzFld(property = "state", gridOrder = 8) //
}), @BzGrp(name = "联系方式", code = "contact"//
, fields = { @BzFld(property = "username", gridOrder = 1) //
		, @BzFld(property = "sex") //
		, @BzFld(property = "tel") //
		, @BzFld(property = "qq") //
		, @BzFld(property = "postcode") //
		, @BzFld(property = "address", gridOrder = 2) //

}) //
}// end groups
)
public class PhotoActivity extends SFTBizComponent implements IActivityEntry {
	@BzFld(name = "活动分类", mode = "c:HM e:M *:N v:S", options = "['type eq 1']")
	@ManyToOne
	protected ActivityCatalog catalog;

	@BzFld(name = "上传作品", mode = "c:M e:M *:N v:S", uploadType = "*.bmp;*.jpg;*.gif;*.png;")
	protected Upload image;

	@BzFld(name = "作品状态", mode = "bu:M *:N v:S", options = "0:未审核,1:显示,9:屏蔽")
	protected byte state;

	@Column(length = 32)
	@BzFld(name = "IP地址", mode = "*:N v:S", privacy = true)
	protected String createdIP;

	@Column(length = 32)
	@BzFld(name = "用户类型", mode = "*:N v:S")
	protected String usertype;

	@BzFld(name = "点击次数", mode = "*:N v:S")
	protected int clickNum;

	@BzFld(name = "评论次数", mode = "*:N v:S")
	protected int commentNum;

	@BzFld(name = "赞成票数", mode = "*:N v:S")
	protected int voteAgreeNum;

	@BzFld(name = "反对票数", mode = "*:N v:S")
	protected int voteOpposeNum;

	/*
	 * 联系方式
	 */
	@BzFld(name = "真实姓名", mode = "c:M *:N v:S", privacy = true)
	private String username;

	@BzFld(name = "性别", mode = "c:M *:N v:S", options = "1:男,0:女", disabledNavi = true)
	private Byte sex;

	@Column(length = 32)
	@BzFld(name = "手机号码", mode = "c:M *:N v:S", privacy = true)
	private String tel;

	@Column(length = 32)
	@BzFld(name = "QQ号码", mode = "c:M *:N v:S", privacy = true)
	private String qq;

	@Column(length = 12)
	@BzFld(name = "邮政编码", mode = "c:M *:N v:S")
	private String postcode;

	@Column(length = 256)
	@BzFld(name = "通讯地址", mode = "c:M *:N v:S", privacy = true)
	private String address;

	public ActivityCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(ActivityCatalog catalog) {
		this.catalog = catalog;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
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

	public int getClickNum() {
		return clickNum;
	}

	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public Upload getImage() {
		return image;
	}

	public void setImage(Upload image) {
		this.image = image;
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

	public int getVoteAgreeNum() {
		return voteAgreeNum;
	}

	public void setVoteAgreeNum(int voteAgreeNum) {
		this.voteAgreeNum = voteAgreeNum;
	}

	public int getVoteOpposeNum() {
		return voteOpposeNum;
	}

	public void setVoteOpposeNum(int voteOpposeNum) {
		this.voteOpposeNum = voteOpposeNum;
	}
}
