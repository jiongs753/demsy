package com.kmetop.demsy.comlib.impl.sft.dic;

import static com.kmetop.demsy.comlib.LibConst.BIZCATA_BASE;

import java.util.List;

import javax.persistence.Column;
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
@BzSys(name = "字典数据维护", code = "Dic", catalog = BIZCATA_BASE, orderby = 101, buildin = true//
, actions = { @BzAct(jsonData = "CommonBizAction.data.js"), //
		@BzAct(name = "设置", typeCode = BizConst.TYPE_BZ_AUTO_MAKED_UPDATE_MENUS, mode = "set") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(property = "category") //
		, @BzFld(name = "名称", property = "name", mode = "c:M e:M", tostring = true)//
		, @BzFld(name = "编号", property = "code", mode = "c:M e:M")//
		, @BzFld(property = "extCode") //
		, @BzFld(name = "描述", property = "desc", gridField = false) //
		, @BzFld(name = "停用状态", property = "disabled", disabledNavi = true, mode = "set:E", options = "1:停用,0:启用") //
		, @BzFld(name = "人工顺序", property = "orderby", mode = "*:N v:P", gridField = false) //
		, @BzFld(name = "创建时间", property = "created", mode = "*:N v:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:N v:P", gridField = false) //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:N v:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:P", gridField = false) //

}) //
}// end groups
)
public class Dic extends SFTBizComponent {
	public static final int MASK_DISABLED = 4;// 2^2

	@ManyToOne
	@BzFld(name = "字典分类")
	protected DicCategory category;

	@ManyToOne
	protected Dic parent;

	@OneToMany(mappedBy = "parent")
	protected List<Dic> children;

	@Column(length = 255)
	@BzFld(name = "扩展编号")
	protected String extCode;

	public boolean isDisabled() {
		return super.getMask(MASK_DISABLED);
	}

	public Boolean getDisabled() {
		return this.isDisabled();
	}

	public void setDisabled(boolean disabled) {
		super.setMask(MASK_DISABLED, disabled);
	}

	public DicCategory getCategory() {
		return category;
	}

	public void setCategory(DicCategory category) {
		this.category = category;
	}

	public String getExtCode() {
		return extCode;
	}

	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}

	public void setParent(Dic parent) {
		this.parent = parent;
	}

	public void setChildren(List<Dic> children) {
		this.children = children;
	}
}
