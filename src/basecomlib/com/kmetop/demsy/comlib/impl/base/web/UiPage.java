package com.kmetop.demsy.comlib.impl.base.web;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_PAGE;
import static com.kmetop.demsy.comlib.LibConst.ORDER_UIUDF_TEMPLATE;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.ui.IPage;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "页面自定义", code = BIZSYS_UIUDF_PAGE, catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_UIUDF_TEMPLATE//
, actions = { @BzAct(name = "排版", typeCode = TYPE_BZFORM_EDIT, mode = "layout", targetWindow = "_blank", targetUrl = MvcConst.URL_ADMIN_UI) //
		, @BzAct(name = "新增页面", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(name = "编辑样式", typeCode = TYPE_BZFORM_EDIT, mode = "e1") //
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "catalog", gridOrder = 8) //
		, @BzFld(property = "type", gridOrder = 6) //
		, @BzFld(property = "parent", gridOrder = 5) //
		, @BzFld(property = "name", name = "模版名称", mode = "c:M e:M e1:N", gridOrder = 1)//
		, @BzFld(property = "code", name = "模版编号", gridOrder = 7, mode = "e1:N") //
		, @BzFld(property = "usage", gridOrder = 4) //
		, @BzFld(property = "style", gridOrder = 3) //
		, @BzFld(property = "pageWidth") //
		, @BzFld(property = "pageHeight") //
		, @BzFld(property = "keywords", gridOrder = 2) //
		, @BzFld(property = "desc", name = "页面摘要", mode = "e1:N") //
// , @BzFld(property = "created", name = "创建时间", mode = "*:P") //
// , @BzFld(property = "updated", name = "更新时间", mode = "*:P") //
}), @BzGrp(name = "样式设计", code = "styles"//
, fields = { @BzFld(property = "styleItems") //
}) }// end groups
)
public class UiPage extends BizComponent implements IPage {

	@ManyToOne
	@BzFld(name = "界面分类", disabledNavi = true, groupBy = true, mode = "c:M e:M e1:N")
	@Prop("uiCatalog")
	protected UiCatalog catalog;// 每个网站版本的网站由多个页面模版组成

	@ManyToOne
	@BzFld(name = "上级分类", cascadeMode = "catalog:*:catalog", options = "['type = 1']", mode = "e1:N")
	protected UiPage parent;

	@ManyToOne
	@BzFld(name = "引用样式", disabledNavi = true, cascadeMode = "catalog:*:catalog", mode = "e1:H")
	protected UiCatalogStyle style;

	@Column(length = 2000)
	@BzFld(name = "CSS样式", gridField = false, uiTemplate = "ui.widget.ext.cssDesigner", mode = "*:N e:H e1:E v:S")
	protected FakeSubSystem<StyleItem> styleItems;

	@BzFld(name = "页面类型", disabledNavi = true, options = "0:模版,1:分类,2:板块引用", mode = "c:M e:M e1:N")
	protected byte type;

	@BzFld(name = "页面用途", disabledNavi = true, cascadeMode = "type:0:E", mode = "e1:N", options = "0:网页模版,1:网站首页,99:后台登录页,100:后台首页")
	protected byte usage;

	@Column(length = 10)
	@BzFld(name = "页面宽度", cascadeMode = "type:0,2:E", mode = "e1:N")
	protected String pageWidth;// 0:表示自动100%

	@Column(length = 10)
	@BzFld(name = "页面高度", cascadeMode = "type:0,2:E", mode = "e1:N")
	protected String pageHeight;

	@OneToMany(mappedBy = "parent")
	protected List<UiPage> children;

	@Column(length = 256)
	@BzFld(name = "页面模版", cascadeMode = "type:0,2:E", mode = "e1:N")
	protected String uiTemplate;

	@Column(length = 512)
	@BzFld(name = "关键字", cascadeMode = "type:0:E", mode = "e1:N")
	protected String keywords;

	public UiCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(UiCatalog uiCatalog) {
		this.catalog = uiCatalog;
	}

	public UiPage getParent() {
		return parent;
	}

	public void setParent(UiPage parent) {
		this.parent = parent;
	}

	public UiCatalogStyle getStyle() {
		return style;
	}

	public void setStyle(UiCatalogStyle style) {
		this.style = style;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getUsage() {
		return usage;
	}

	public void setUsage(byte usage) {
		this.usage = usage;
	}

	public List<UiPage> getChildren() {
		return children;
	}

	public void setChildren(List<UiPage> children) {
		this.children = children;
	}

	public String getUiTemplate() {
		return uiTemplate;
	}

	public void setUiTemplate(String uiTemplate) {
		this.uiTemplate = uiTemplate;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getPageHeight() {
		return pageHeight;
	}

	public void setPageWidth(String pageWidth) {
		this.pageWidth = pageWidth;
	}

	public void setPageHeight(String pageHeight) {
		this.pageHeight = pageHeight;
	}

	public String getPageWidth() {
		return pageWidth;
	}

	public FakeSubSystem<StyleItem> getStyleItems() {
		return styleItems;
	}

	public void setStyleItems(FakeSubSystem<StyleItem> styleItems) {
		this.styleItems = styleItems;
	}

}
