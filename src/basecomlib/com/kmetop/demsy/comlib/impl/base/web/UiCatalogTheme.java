package com.kmetop.demsy.comlib.impl.base.web;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.ORDER_UIUDF_CATALOG_THEME;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "主题管理", catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_UIUDF_CATALOG_THEME//
, actions = { @BzAct(name = "新增主题", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "name", name = "主题名称", mode = "c:M e:M")//
		, @BzFld(property = "code", name = "主题编号", mode = "c:E e:E")//
		, @BzFld(property = "catalog") //
		, @BzFld(property = "parent") //
		, @BzFld(property = "desc", gridField = false, name = "全局样式文本") //
		, @BzFld(property = "created", name = "创建时间", mode = "*:P") //
		, @BzFld(property = "updated", name = "更新时间", mode = "*:P") //
}) }// end groups
)
public class UiCatalogTheme extends BizComponent {

	@ManyToOne
	@BzFld(name = "界面分类", groupBy = true, mode = "c:M e:M")
	@Prop("uiCatalog")
	protected UiCatalog catalog;

	@ManyToOne
	@BzFld(name = "上级主题", cascadeMode = "catalog:*:catalog")
	protected UiCatalogTheme parent;

	@BzFld(name = "默认风格")
	protected boolean defaults;

	@OneToMany(mappedBy = "parent")
	protected List<UiCatalogTheme> children;

	public UiCatalog getCatalog() {
		return catalog;
	}

	public UiCatalogTheme getParent() {
		return parent;
	}

	public boolean isDefaults() {
		return defaults;
	}

	public void setCatalog(UiCatalog uiCatalog) {
		this.catalog = uiCatalog;
	}

	public void setParent(UiCatalogTheme parent) {
		this.parent = parent;
	}

	public void setDefaults(boolean defaults) {
		this.defaults = defaults;
	}

	public List<UiCatalogTheme> getChildren() {
		return children;
	}

	public void setChildren(List<UiCatalogTheme> children) {
		this.children = children;
	}
}
