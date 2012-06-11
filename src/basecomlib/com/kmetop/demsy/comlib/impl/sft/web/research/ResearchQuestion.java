package com.kmetop.demsy.comlib.impl.sft.web.research;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_RESEARCH;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.web.IResearchQuestion;
import com.kmetop.demsy.comlib.web.IResearchSubject;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "问题列表", code = IResearchQuestion.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_RESEARCH, buildin = true//
, actions = {
// @BzAct(name = "添加问题", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin =
// "com.kmetop.demsy.plugins.web.SaveResearchQuestion")//
// , @BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", plugin =
// "com.kmetop.demsy.plugins.web.SaveResearchQuestion") //
// , @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
@BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
// , @BzAct(jsonData = "CommonBizAction_orderby.data.js") //
}, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "subject") //
		, @BzFld(property = "name", name = "问题名称", mode = "c:M e:M *:N v:S")//
		, @BzFld(property = "type") //
		, @BzFld(property = "mustable") //
		, @BzFld(property = "result") //
		, @BzFld(property = "optionsJson", gridField = false) //
		, @BzFld(property = "orderby", name = "人工顺序", mode = "*:N v:S") //
		// , @BzFld(property = "desc", name = "问题描述", mode = "c:E e:E *:N v:S",
		// gridField = false) //
		, @BzFld(property = "created", name = "创建时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm", gridField = false) //
		, @BzFld(property = "createdBy", name = "创建帐号", mode = "*:N v:S", gridField = false) //
		, @BzFld(property = "updated", name = "更新时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm", gridField = false) //
		, @BzFld(property = "updatedBy", name = "更新帐号", mode = "*:N v:S", gridField = false) //

}) //
}// end groups
)
public class ResearchQuestion extends SFTBizComponent implements IResearchQuestion {
	@Transient
	private List<ResearchAnswerOption> options;

	@ManyToOne
	@BzFld(name = "调查主题", mode = "c:M e:M *:N v:S", masterMapping = true)
	private ResearchSubject subject;

	@Prop("allowMultiple")
	@BzFld(name = "问题类型", mode = "c:M e:M *:N v:S", options = "0:单选,1:多选,2:问卷")
	private byte type;
	
	@BzFld(name = "是否必填", mode = "c:M e:M *:N v:S", options = "0:可选,1:必填")
	private byte mustable;

	@BzFld(name = "回答次数", mode = "*:N v:S")
	private Long result;

	@Transient
	@Column(length = 512)
	@BzFld(name = "选择项", mode = "c:E e:E *:N v:S", isTransient = true)
	private String optionsJson;

	public ResearchSubject getSubject() {
		return subject;
	}

	public void setSubject(IResearchSubject subject) {
		this.subject = (ResearchSubject) subject;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte selectPolicy) {
		this.type = selectPolicy;
	}

	public Long getResult() {
		return result;
	}

	public void setResult(Long selectedTimes) {
		this.result = selectedTimes;
	}

	public String getOptionsJson() {
		return optionsJson;
	}

	public void setOptionsJson(String optionText) {
		this.optionsJson = optionText;
	}

	public List getOptions() {
		return options;
	}

	public void setOptions(List options) {
		this.options = options;
	}

	public byte getMustable() {
		return mustable;
	}

	public void setMustable(byte mustable) {
		this.mustable = mustable;
	}
}
