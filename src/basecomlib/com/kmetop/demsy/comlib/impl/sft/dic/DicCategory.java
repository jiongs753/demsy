package com.kmetop.demsy.comlib.impl.sft.dic;

import static com.kmetop.demsy.comlib.LibConst.*;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.biz.BizConst;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;

@Entity
@BzSys(name = "字典分类设置", code = "DicCategory", catalog = BIZCATA_BASE, orderby = 100, buildin = true//
, actions = { @BzAct(jsonData = "CommonBizAction.data.js"), //
		@BzAct(name = "设置", typeCode = BizConst.TYPE_BZ_AUTO_MAKED_UPDATE_MENUS, mode = "set") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(name = "字典名称", property = "name", mode = "c:M e:M", tostring = true)//
		, @BzFld(property = "parent") //
		, @BzFld(name = "字典描述", property = "desc", gridField = false) //
		, @BzFld(name = "停用状态", property = "disabled", disabledNavi = true, mode = "set:E", options = "1:停用,0:启用") //
		, @BzFld(name = "人工顺序", property = "orderby", mode = "*:N v:P", gridField = false) //
		, @BzFld(name = "创建时间", property = "created", mode = "*:N v:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:N v:P", gridField = false) //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:N v:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:P", gridField = false) //

}) //
}// end groups
)
public class DicCategory extends SFTBizComponent {

	@OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
	protected List<Dic> dics;

	@ManyToOne
	@BzFld(name = "上级分类")
	protected DicCategory parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
	protected List<DicCategory> children;

	public List getDics() {
		return dics;
	}

	public void setDics(List<Dic> dics) {
		this.dics = dics;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List<DicCategory> children) {
		this.children = children;
	}

	public DicCategory getParent() {
		return parent;
	}

	public void setParent(DicCategory parent) {
		this.parent = parent;
	}
}
