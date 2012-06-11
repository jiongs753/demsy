package com.kmetop.demsy.comlib.impl.sft.web.research;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_RESEARCH;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.web.IResearchQuestion;
import com.kmetop.demsy.comlib.web.IResearchSubject;

@Entity
@BzSys(name = "网站调查管理", code = IResearchSubject.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_RESEARCH, buildin = true//
, actions = {
		@BzAct(name = "添加调查", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.web.SaveResearchSubject")//
		,
		@BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", plugin = "com.kmetop.demsy.plugins.web.SaveResearchSubject") //
		,
		@BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
		,
		@BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v", plugin = "com.kmetop.demsy.plugins.web.LoadResearchSubject") //
		, @BzAct(jsonData = "CommonBizAction_orderby.data.js") //
}, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
		@BzFld(property = "category") //
		,
		@BzFld(property = "name", name = "主题名称", mode = "c:M e:M *:N v:S")//
		,
		@BzFld(property = "entryPolicy") //
		,
		@BzFld(property = "entryTimes") //
		,
		@BzFld(property = "viewPolicy") //
		,
		@BzFld(property = "expiredFrom") //
		,
		@BzFld(property = "expiredTo") //
		,
		@BzFld(property = "image") //
		,
		@BzFld(property = "result") //
		,
		@BzFld(property = "questionsJson") //
		,
		@BzFld(property = "orderby", name = "人工顺序", mode = "*:N v:S") //
		// , @BzFld(property = "desc", name = "调查描述", mode = "c:E e:E *:N v:S",
		// gridField = false) //
		,
		@BzFld(property = "created", name = "创建时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm", gridField = false) //
		,
		@BzFld(property = "createdBy", name = "创建帐号", mode = "*:N v:S", gridField = false) //
		,
		@BzFld(property = "updated", name = "更新时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm", gridField = false) //
		, @BzFld(property = "updatedBy", name = "更新帐号", mode = "*:N v:S", gridField = false) //

}) //
}// end groups
)
public class ResearchSubject extends SFTBizComponent implements IResearchSubject {

	@Transient
	private List<IResearchQuestion> questions;

	@ManyToOne
	@BzFld(name = "调查分类", mode = "c:E e:E *:N v:S")
	private ResearchCategory category;

	@BzFld(name = "参与策略", mode = "c:E e:E *:N v:S", options = "0:匿名参与,1:登录参与")
	private byte entryPolicy;

	@BzFld(name = "限制次数", mode = "c:E e:E *:N v:S", desc = "限制每人最多只能参与多少次调查？")
	private byte entryTimes;

	@BzFld(name = "查看策略", mode = "c:E e:E *:N v:S", options = "0:禁止查看,1:允许查看,2:登录查看,3:管理员查看,4:超级管理员查看")
	private byte viewPolicy = 1;

	@BzFld(name = "开始时间", mode = "c:E e:E *:N v:S", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expiredFrom;

	@BzFld(name = "截止时间", mode = "c:E e:E *:N v:S", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expiredTo;

	@BzFld(name = "链接图片", mode = "c:E e:E *:N v:S", uploadType = "*.bmp;*.jpg;*.gif;*.png;*.swf", gridField = false)
	protected Upload image;

	@BzFld(name = "参与次数", mode = "*:N v:S")
	private Long result;

	// 多个问题换行分隔
	@Transient
	@Column(length = 512)
	@BzFld(name = "问题选项", mode = "c:E e:E *:N v:S", isTransient = true, gridField = false, uiTemplate = "ui.json.ResearchQuestionJsonSubSystem")
	private String questionsJson;

	public ResearchCategory getCategory() {
		return category;
	}

	public void setCategory(ResearchCategory category) {
		this.category = category;
	}

	public byte getEntryPolicy() {
		return entryPolicy;
	}

	public void setEntryPolicy(byte entryPolicy) {
		this.entryPolicy = entryPolicy;
	}

	public byte getEntryTimes() {
		return entryTimes;
	}

	public void setEntryTimes(byte entryTimes) {
		this.entryTimes = entryTimes;
	}

	public byte getViewPolicy() {
		return viewPolicy;
	}

	public void setViewPolicy(byte viewPolicy) {
		this.viewPolicy = viewPolicy;
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

	public String getQuestionsJson() {
		return questionsJson;
	}

	public void setQuestionsJson(String questions) {
		this.questionsJson = questions;
	}

	public Long getResult() {
		return result;
	}

	public void setResult(Long result) {
		this.result = result;
	}

	public List<IResearchQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<IResearchQuestion> questions) {
		this.questions = questions;
	}

	public Upload getImage() {
		return image;
	}

	public void setImage(Upload image) {
		this.image = image;
	}
}
