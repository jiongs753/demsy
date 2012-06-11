package com.kmetop.demsy.comlib.impl.sft.web.research;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_ADD_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_RESEARCH;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.web.IResearchQuestion;
import com.kmetop.demsy.comlib.web.IResearchResult;
import com.kmetop.demsy.comlib.web.IResearchSubject;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "调查结果", code = IResearchResult.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_RESEARCH, buildin = true//
, actions = { @BzAct(name = "批量添加", typeCode = TYPE_BZFORM_ADD_N, mode = "c_n", disabled = true, plugin = "com.kmetop.demsy.plugins.web.SaveResearchResult", info = "提交调查成功！") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
}, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "subject") //
		, @BzFld(property = "question") //
		, @BzFld(property = "option")//
		, @BzFld(property = "answerText") //
		, @BzFld(property = "createdIP") //
		, @BzFld(property = "created", name = "参与时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm", gridField = false) //
		, @BzFld(property = "createdBy", name = "参与帐号", mode = "*:N v:S", gridField = false) //

}) //
}// end groups
)
public class ResearchResult extends SFTBizComponent implements IResearchResult {
	@ManyToOne
	@BzFld(name = "调查主题", mode = "c_n:E *:N v:S", masterMapping = true)
	private ResearchSubject subject;

	@ManyToOne
	@BzFld(name = "调查问题", mode = "c_n:E *:N v:S", masterMapping = true, disabledNavi = true)
	private ResearchQuestion question;

	@Prop("answerOption")
	@ManyToOne
	@BzFld(name = "选择项", mode = "c_n:E *:N v:S", masterMapping = true, disabledNavi = true)
	private ResearchAnswerOption option;

	@Column(length = 255)
	@BzFld(name = "问卷回答", mode = "c_n:E *:N v:S", masterMapping = true, disabledNavi = true)
	private String answerText;

	@Column(length = 32)
	@Prop("remoteIp")
	@BzFld(name = "IP地址", mode = "*:N v:S", masterMapping = true, gridField = false)
	private String createdIP;

	@Prop("value")
	@BzFld(name = "投票次数", mode = "*:N v:S")
	private Long result = 1l;

	public ResearchSubject getSubject() {
		return subject;
	}

	public void setSubject(IResearchSubject subject) {
		this.subject = (ResearchSubject) subject;
	}

	public ResearchQuestion getQuestion() {
		return question;
	}

	public void setQuestion(IResearchQuestion question) {
		this.question = (ResearchQuestion) question;
	}

	public ResearchAnswerOption getOption() {
		return option;
	}

	public void setOption(ResearchAnswerOption answerOption) {
		this.option = answerOption;
	}

	public Long getResult() {
		return result;
	}

	public void setResult(Long value) {
		this.result = value;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String createdIP) {
		this.createdIP = createdIP;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

}
