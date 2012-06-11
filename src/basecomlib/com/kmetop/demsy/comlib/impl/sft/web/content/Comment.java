package com.kmetop.demsy.comlib.impl.sft.web.content;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_COMMENT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.base.security.Module;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "网站留言管理", code = "Comment", catalog = BIZCATA_WEB, orderby = ORDER_WEB_COMMENT, buildin = true//
, actions = { @BzAct(name = "添加", typeCode = TYPE_BZFORM_NEW, mode = "c", disabled = true, info = "提交成功！审核通过后发布。", plugin = "com.kmetop.demsy.plugins.web.SaveComment")//
		, @BzAct(name = "审核", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu")//
		, @BzAct(name = "回复", typeCode = TYPE_BZFORM_EDIT, mode = "e") //
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(property = "module", gridOrder = 1) //
		, @BzFld(property = "name", name = "主题", mode = "*:N v:S", gridOrder = 2) //
		, @BzFld(property = "commenter") //
		, @BzFld(property = "createdIP", gridOrder = 5) //
		, @BzFld(property = "created", name = "时间", pattern = "yyyy-MM-dd hh:mm:ss", mode = "bu:N *:S", gridOrder = 6) //
		, @BzFld(property = "status", gridOrder = 4) //
		, @BzFld(property = "content", gridOrder = 3) //
		, @BzFld(property = "replyContent") //
		, @BzFld(property = "subjectID") //
}) //
}// end groups
)
public class Comment extends SFTBizComponent {
	public static final int STATUS_UNCHECKED = 0;

	public static final int STATUS_SHOWN = 2;

	public static final int STATUS_HIDDEN = 99;

	@BzFld(name = "网友", mode = "c:E bu:N *:S")
	protected String commenter;// 评论员

	@Column(length = 2000)
	@BzFld(name = "内容", mode = "c:M bu:N *:S")
	protected String content;// 内容

	protected Boolean hidden;

	@BzFld(name = "状态", mode = "e:E bu:E *:S", options = "0:未审核,2:显示,99:屏蔽")
	@Prop("commentStatus")
	protected byte status;// 评论状态

	@Column(length = 32)
	@Prop("ipAddress")
	@BzFld(name = "IP地址", mode = "bu:N *:S")
	protected String createdIP;// 远程IP地址

	@Column(length = 2000)
	@BzFld(name = "回复内容", mode = "e:M bu:N *:S")
	protected String replyContent;// 回复

	@ManyToOne
	@BzFld(name = "模块", disabledNavi = true, mode = "c:M bu:N *:S")
	protected Module module;

	@BzFld(name = "主题", disabledNavi = true, mode = "c:M bu:N *:S")
	protected Long subjectID;

	@ManyToOne
	@BzFld(name = "标题", disabledNavi = true, mode = "c:M bu:N *:S")
	protected WebContent webContent;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Byte getStatus() {
		return status;
	}

	public void setCommentStatus(Byte commentStatus) {
		this.status = commentStatus;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String ipAddress) {
		this.createdIP = ipAddress;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Long getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(Long data) {
		this.subjectID = data;
	}

	public String getCommenter() {
		return commenter;
	}

	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}

	public WebContent getWebContent() {
		return webContent;
	}

}
