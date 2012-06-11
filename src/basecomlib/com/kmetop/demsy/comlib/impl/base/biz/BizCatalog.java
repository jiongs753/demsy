package com.kmetop.demsy.comlib.impl.base.biz;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_BZUDF_CATALOG;
import static com.kmetop.demsy.comlib.LibConst.ORDER_BZUDF_CATALOG;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.IBizCatalog;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@BzSys(name = "系统分类", code = BIZSYS_BZUDF_CATALOG, catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_BZUDF_CATALOG, buildin = true//
, actions = { @BzAct(name = "新增分类", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.biz.CreateBizCatalog")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "分类名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "分类编号", property = "code") //
		, @BzFld(name = "上级分类", property = "parent", refrenceSystem = BIZSYS_BZUDF_CATALOG) //
		, @BzFld(name = "上级分类", property = "parent.name", mode="*:N") //
		, @BzFld(name = "人工顺序", property = "orderby") //
		, @BzFld(name = "分类描述", property = "desc") //
		, @BzFld(name = "内置状态", property = "buildin", disabledNavi = true, mode = "*:N") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:P") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
, jsonData = "BizCatalog.data.js"//
)
public class BizCatalog extends BizComponent implements IBizCatalog<BizCatalog> {
	@ManyToOne
	private BizCatalog parent;

	@OneToMany(mappedBy = "parent")
	private List<BizCatalog> children;

	public BizCatalog getParent() {
		return parent;
	}

	public void setParent(BizCatalog parent) {
		this.parent = parent;
	}

	public List<BizCatalog> getChildren() {
		return children;
	}

	public void setChildren(List<BizCatalog> children) {
		this.children = children;
	}
}
