package com.kmetop.demsy.comlib.impl.base.web;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_WEB_VOTE;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_VOTE;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@BzSys(name = "网站投票管理", code = BIZSYS_WEB_VOTE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_VOTE, buildin = true//
, actions = { @BzAct(name = "添加", typeCode = TYPE_BZFORM_NEW, mode = "c", disabled = true, info = "投票成功！", plugin = "com.kmetop.demsy.plugins.web.SaveVote")//
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "name", name = "主题", mode = "c:M *:N v:S") //
		, @BzFld(property = "moduleID") //
		, @BzFld(property = "subjectID") //
		, @BzFld(property = "score") //
		, @BzFld(property = "type") //
		, @BzFld(property = "created", name = "投票时间", pattern = "yyyy-MM-dd hh:mm:ss", mode = "bu:N *:S") //
		, @BzFld(property = "createdBy", name = "投票人") //
		, @BzFld(property = "createdIP") //
}) //
}// end groups
)
public class Vote extends BizComponent {
	@BzFld(name = "模块ID", disabledNavi = true, mode = "c:M bu:N *:S")
	protected Long moduleID;

	@BzFld(name = "主题ID", disabledNavi = true, mode = "c:M bu:N *:S")
	protected Long subjectID;

	@BzFld(name = "分值", mode = "c:M bu:N *:S")
	protected int score;

	@BzFld(name = "类型", options = "0:反对,1:支持", mode = "c:M bu:N *:S")
	protected byte type;

	@Column(length = 32)
	@BzFld(name = "IP地址", mode = "bu:N *:S")
	protected String createdIP;// 远程IP地址

	public Long getModuleID() {
		return moduleID;
	}

	public void setModuleID(Long moduleID) {
		this.moduleID = moduleID;
	}

	public Long getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(Long subjectID) {
		this.subjectID = subjectID;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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
}
