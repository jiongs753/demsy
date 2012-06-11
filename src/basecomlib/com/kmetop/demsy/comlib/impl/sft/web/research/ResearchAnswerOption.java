package com.kmetop.demsy.comlib.impl.sft.web.research;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_RESEARCH;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.web.IResearchOption;
import com.kmetop.demsy.comlib.web.IResearchQuestion;
import com.kmetop.demsy.comlib.web.IResearchSubject;

@Entity
@BzSys(name = "选项列表", code = IResearchOption.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_RESEARCH, buildin = true//
, actions = { @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
// @BzAct(name = "添加选项", typeCode = TYPE_BZFORM_NEW, mode = "c")//
// , @BzAct(jsonData = "CommonBizAction.data.js") //
}, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "subject") //
		, @BzFld(property = "question") //
		, @BzFld(property = "name", name = "选项名称", mode = "c:M e:M *:N v:S")//
		, @BzFld(property = "type") //
		, @BzFld(property = "orderby", name = "人工顺序", mode = "*:N v:S") //
		, @BzFld(property = "result") //
		// , @BzFld(property = "desc", name = "选项描述", mode = "c:E e:E *:N v:S",
		// gridField = false) //
		, @BzFld(property = "created", name = "创建时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm", gridField = false) //
		, @BzFld(property = "createdBy", name = "创建帐号", mode = "*:N v:S", gridField = false) //
		, @BzFld(property = "updated", name = "更新时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm", gridField = false) //
		, @BzFld(property = "updatedBy", name = "更新帐号", mode = "*:N v:S", gridField = false) //
}) //
}// end groups
)
public class ResearchAnswerOption extends SFTBizComponent implements IResearchOption {
	@ManyToOne
	@BzFld(name = "调查主题", mode = "c:M e:M *:N v:S", masterMapping = true)
	private ResearchSubject subject;

	@ManyToOne
	@BzFld(name = "问题名称", mode = "c:M e:M *:N v:S", masterMapping = true, disabledNavi = true)
	private ResearchQuestion question;

	@BzFld(name = "回答方式", mode = "c:M e:M *:N v:S", options = "0:选择,1:输入")
	private byte type;

	@BzFld(name = "选择次数", mode = "*:N v:S")
	private Long result;

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

	public Long getResult() {
		return result;
	}

	public void setResult(Long selectedTimes) {
		this.result = selectedTimes;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}
