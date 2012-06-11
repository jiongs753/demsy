package com.kmetop.demsy.comlib.impl.sft.web.research;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_RESEARCH_CATALOG;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
@BzSys(name = "网站调查分类", code = "_web_research_category", catalog = BIZCATA_WEB, orderby = ORDER_WEB_RESEARCH_CATALOG, buildin = true//
, actions = { @BzAct(name = "添加分类", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "name", name = "分类名称", mode = "c:M e:M *:N v:S")//
		, @BzFld(property = "parent") //
		, @BzFld(property = "orderby", name = "人工顺序", mode = "*:N v:S") //
		, @BzFld(property = "desc", name = "分类描述", mode = "c:E e:E *:N v:S", gridField = false) //
		, @BzFld(property = "created", name = "创建时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm", gridField = false) //
		, @BzFld(property = "createdBy", name = "创建帐号", mode = "*:N v:S", gridField = false) //
		, @BzFld(property = "updated", name = "更新时间", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm", gridField = false) //
		, @BzFld(property = "updatedBy", name = "更新帐号", mode = "*:N v:S", gridField = false) //

}) //
}// end groups
)
public class ResearchCategory extends SFTBizComponent {

	@ManyToOne
	@BzFld(name = "上级分类", mode = "c:E e:E *:N v:S")
	protected ResearchCategory parent;

	public ResearchCategory getParent() {
		return parent;
	}

	public void setParent(ResearchCategory parent) {
		this.parent = parent;
	}

}
