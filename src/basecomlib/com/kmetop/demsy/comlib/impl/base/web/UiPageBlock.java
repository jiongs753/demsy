package com.kmetop.demsy.comlib.impl.base.web;

import static com.kmetop.demsy.biz.BizConst.*;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_UDF_CONSOLE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_UIUDF_PAGE_BLOCK;
import static com.kmetop.demsy.comlib.LibConst.ORDER_UIUDF_TEMPLATE_BLOCK;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.CssPosition;
import com.kmetop.demsy.comlib.biz.field.Dataset;
import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.impl.base.lib.UiModelLib;
import com.kmetop.demsy.comlib.ui.IPageBlock;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "页面板块", code = BIZSYS_UIUDF_PAGE_BLOCK, catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_UIUDF_TEMPLATE_BLOCK//
, layout = 1, actions = { @BzAct(name = "新增板块", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(name = "编辑样式", typeCode = TYPE_BZFORM_EDIT, mode = "e1") //
		, @BzAct(name = "批量修改", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
		, @BzAct(name = "排版", typeCode = TYPE_BZFORM_NEW, mode = "c1", disabled = true) //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "catalog") //
		, @BzFld(property = "page", gridOrder = 8) //
		, @BzFld(property = "viewType", gridOrder = 4) //
		, @BzFld(property = "parent", gridOrder = 7) //
		, @BzFld(property = "style")//
		, @BzFld(property = "name", name = "板块名称", gridOrder = 1, mode = "e1:N")//
		, @BzFld(property = "dataset") //
		, @BzFld(property = "dataset.rules2", name = "数据源", mode = "*:N", isTransient = true, gridOrder = 2) //
}), @BzGrp(name = "图片设置", code = "image"//
, fields = { @BzFld(property = "imageWidth") //
		, @BzFld(property = "imageHeight") //
		, @BzFld(property = "allowEmptyImg") //
		, @BzFld(property = "autoCutImage", name = "剪切图片", disabledNavi = true, options = "1:自动剪切,0:不剪切", mode = "e1:N") //
}), @BzGrp(name = "列表设置", code = "table"//
, fields = { @BzFld(property = "titleEnabled") //
		, @BzFld(property = "titleLink") //
		, @BzFld(property = "titleLinkTarget")//
		, @BzFld(property = "link")//
		, @BzFld(property = "linkTarget")//
		, @BzFld(property = "titleLength")//
		, @BzFld(property = "horizontal")//
		, @BzFld(property = "cellCount")//
		, @BzFld(property = "scrollDirection") //
		, @BzFld(property = "naviButton") //
		, @BzFld(property = "showMoreBtn", name = "更多按钮", disabledNavi = true, options = "1:显示,0:隐藏", mode = "e1:N", desc = "用于列表板块，判断是否在标题后面显示“更多...”按钮？") //
		, @BzFld(property = "fillBlank", name = "填充空白", disabledNavi = true, mode = "e1:N", desc = "用于列表类板块，判断实际数据行数小于列表所需行数时是否用空白行填充列表？") //
}), @BzGrp(name = "板块布局", code = "inlineStyle"//
, fields = { @BzFld(property = "inlineStyle") //
		, @BzFld(name = "布局顺序", property = "orderby", uiTemplate = "ui.widget.field.Spinner", mode = "e1:N") //
		, @BzFld(property = "position") //
}), @BzGrp(name = "高级属性", code = "others"//
, fields = { @BzFld(property = "viewController") //
		, @BzFld(property = "viewTemplate") //
		, @BzFld(property = "viewExpression") //
		, @BzFld(property = "viewPage") //
		, @BzFld(property = "params") //
		, @BzFld(property = "placeHolder", mode = "e1:N", name = "占位符", disabledNavi = true)//
		, @BzFld(property = "ajaxLoad") //
		, @BzFld(name = "停用状态", property = "disabled", disabledNavi = true, options = "1:停用,0:启用", mode = "e1:N") //
// , @BzFld(property = "type") //
// , @BzFld(name = "内置状态", property = "buildin", disabledNavi = true,
// mode = "*:N") //
// , @BzFld(name = "创建时间", property = "created", mode = "*:N v:P") //
// , @BzFld(name = "更新时间", property = "updated", mode = "*:N v:P") //
// , @BzFld(name = "创建帐号", property = "createdBy", mode = "*:N v:P") //
// , @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:P") //
}), @BzGrp(name = "样式设计", code = "styles"//
, fields = { @BzFld(property = "styleItems") //
}) // end group
}// end groups
)
public class UiPageBlock extends BizComponent implements IPageBlock {

	@ManyToOne
	@BzFld(name = "界面分类", disabledNavi = true, mode = "c:HM e:HM c1:HM bu:E e1:N")
	@Prop("uiCatalog")
	protected UiCatalog catalog;// 每个网站版本的网站由多个页面模版组成

	@ManyToOne
	@BzFld(name = "所属页面", mode = "c:M e:M c1:M bu:E e1:N", groupBy = true, masterMapping = true, cascadeMode = "catalog:*:catalog", options = "['type in 0,2']")
	@Prop("uiPage")
	protected UiPage page;

	@ManyToOne
	@BzFld(name = "上级板块", disabledNavi = true, mode = "c1:E e1:N", cascadeMode = "page:*:page")
	protected UiPageBlock parent;

	/*
	 * 展现模型
	 */

	// ,9:板块容器
	@BzFld(name = "展现方式", disabledNavi = true, options = "0:内置视图,1:自定义,2:页面引用", mode = "*:H v:P e1:N")
	@Prop("uiModelType")
	protected byte type;

	@ManyToOne
	@BzFld(name = "视图类型", disabledNavi = true, options = "['type in 0']", mode = "c1:E e1:N")
	@Prop("uiModelLib")
	protected UiModelLib viewType;

	@Column(length = 127)
	@BzFld(name = "视图控制器", mode = "c1:E e1:N")
	@Prop("uiDataSource")
	protected String viewController;

	@Column(length = 64)
	@BzFld(name = "视图模版", mode = "c1:E e1:N")
	@Prop("uiTemplate")
	protected String viewTemplate;

	@Column(length = 512)
	@BzFld(name = "视图表达式", mode = "c1:E e1:N")
	protected String viewExpression;

	@ManyToOne
	@BzFld(name = "引用页面", disabledNavi = true, cascadeMode = "catalog:*:catalog", options = "['type in 2']", mode = "c1:E e1:N")
	@Prop("uiModelRefPage")
	protected UiPage viewPage;

	/*
	 * 展现 模块数据集
	 */
	@BzFld(name = "展现数据", mode = "c1:E e1:N")
	protected Dataset dataset;

	@BzFld(name = "板块布局", mode = "c1:E e1:N")
	protected CssPosition position;

	// Link to

	/*
	 * 板块属性
	 */

	@BzFld(name = "排版方向", options = "0:纵向(列),1:横向(行)", disabledNavi = true, mode = "c1:E e1:N")
	private boolean horizontal;

	@BzFld(name = "行(列)大小", uiTemplate = "ui.widget.field.Spinner", mode = "c1:E e1:N")
	private Integer cellCount;

	@BzFld(name = "条目标题长度", uiTemplate = "ui.widget.field.Spinner", mode = "c1:E e1:N")
	private Integer titleLength;

	@ManyToOne
	@BzFld(name = "条目链接", disabledNavi = true, options = "['type in 0']", mode = "c1:E e1:N")
	protected UiPage link;

	@Column(length = 10)
	@BzFld(name = "条目链接目标", options = "_blank:新窗口,_parent:父窗口", disabledNavi = true, mode = "c1:E e1:N")
	protected String linkTarget;

	// 标题选项
	@BzFld(name = "栏目标题", options = "0:隐藏,1:显示", disabledNavi = true, mode = "c1:E e1:N")
	private boolean titleEnabled;

	@ManyToOne
	@BzFld(name = "栏目链接", disabledNavi = true, options = "['type in 0']", mode = "c1:E e1:N")
	protected UiPage titleLink;

	@Column(length = 10)
	@BzFld(name = "栏目链接目标", options = "_blank:新窗口,_parent:父窗口", disabledNavi = true, mode = "c1:E e1:N")
	protected String titleLinkTarget;

	// 图片选项
	@BzFld(name = "图片宽度", disabledNavi = true, uiTemplate = "ui.widget.field.Spinner", mode = "c1:E e1:N")
	private Integer imageWidth;

	@BzFld(name = "图片高度", disabledNavi = true, uiTemplate = "ui.widget.field.Spinner", mode = "c1:E e1:N")
	private Integer imageHeight;

	// 滚动选项
	@Column(length = 16)
	@BzFld(name = "内容滚动", disabledNavi = true, options = "up:向上滚动,left:向左滚动", mode = "c1:E e1:N")
	private String scrollDirection;

	@ManyToOne
	@BzFld(name = "引用样式", disabledNavi = true, cascadeMode = "catalog:*:catalog", mode = "c1:E e1:H")
	@Prop("uiStyle")
	protected UiCatalogStyle style;

	@Column(length = 2000)
	@BzFld(name = "CSS样式", gridField = false, uiTemplate = "ui.widget.ext.cssDesigner", mode = "*:N e:H e1:E v:S")
	protected FakeSubSystem<StyleItem> styleItems;

	@Column(length = 512)
	@BzFld(name = "行内样式", mode = "c1:E e1:N")
	@Prop("style")
	protected String inlineStyle;

	@BzFld(name = "加载方式", disabledNavi = true, options = "1:异步加载,0:同步加载", mode = "c1:E e1:N")
	protected Boolean ajaxLoad;

	@BzFld(name = "图片状态", disabledNavi = true, options = "1:允许空图片,0:禁止空图片", mode = "c1:E e1:N")
	protected boolean allowEmptyImg;

	@BzFld(name = "分页按钮", disabledNavi = true, options = "1:显示,0:隐藏", mode = "c1:E e1:N", desc = "用于列表类板块，判断是否在列表中显示分页按钮？")
	protected boolean naviButton;

	@BzFld(name = "板块参数", mode = "c1:E e1:N")
	protected String params;

	public Integer getTitleLength() {
		return titleLength;
	}

	public void setTitleLength(Integer titleLength) {
		this.titleLength = titleLength;
	}

	public UiCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(UiCatalog catalog) {
		this.catalog = catalog;
	}

	public UiPage getPage() {
		return page;
	}

	public void setPage(UiPage page) {
		this.page = page;
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

	public void setType(byte modelType) {
		this.type = modelType;
	}

	public UiModelLib getViewType() {
		return viewType;
	}

	public void setViewType(UiModelLib modelLib) {
		this.viewType = modelLib;
	}

	public String getViewTemplate() {
		return viewTemplate;
	}

	public void setViewTemplate(String modelTemplate) {
		this.viewTemplate = modelTemplate;
	}

	public UiPage getViewPage() {
		return viewPage;
	}

	public void setViewPage(UiPage modelRefPage) {
		this.viewPage = modelRefPage;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public CssPosition getPosition() {
		return position;
	}

	public void setPosition(CssPosition position) {
		this.position = position;
	}

	public UiPage getTitleLink() {
		return titleLink;
	}

	public UiPage getLink() {
		return link;
	}

	public void setTitleLink(UiPage catalogLink) {
		this.titleLink = catalogLink;
	}

	public void setLink(UiPage link) {
		this.link = link;
	}

	public String getViewController() {
		return viewController;
	}

	public void setViewController(String uiDataSource) {
		this.viewController = uiDataSource;
	}

	public boolean getTitleEnabled() {
		return titleEnabled;
	}

	public void setTitleEnabled(boolean titleEnabled) {
		this.titleEnabled = titleEnabled;
	}

	public Integer getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}

	public Integer getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}

	public String getScrollDirection() {
		return scrollDirection;
	}

	public void setScrollDirection(String scrollDirection) {
		this.scrollDirection = scrollDirection;
	}

	public boolean getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public Integer getCellCount() {
		return cellCount;
	}

	public void setCellCount(Integer count) {
		this.cellCount = count;
	}

	public String getInlineStyle() {
		return inlineStyle;
	}

	public void setInlineStyle(String inlineStyle) {
		this.inlineStyle = inlineStyle;
	}

	public Boolean getAjaxLoad() {
		return ajaxLoad;
	}

	public void setAjaxLoad(Boolean ajaxLoad) {
		this.ajaxLoad = ajaxLoad;
	}

	public UiPageBlock getParent() {
		return parent;
	}

	public UiPageBlock getParent(boolean lazy) {
		if (lazy) {
			return getParent();
		}
		return parent;
	}

	public void setParent(UiPageBlock parent) {
		this.parent = parent;
	}

	public String toString() {
		if (!Str.isEmpty(getName())) {
			return this.getName();
		}

		if (dataset != null && !Str.isEmpty(dataset.getRules2())) {
			return dataset.getRules2();
		}

		if (this.getViewType() != null)
			return viewType.toString();

		return super.toString();
	}

	public String getLinkTarget() {
		return linkTarget;
	}

	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}

	public String getTitleLinkTarget() {
		return titleLinkTarget;
	}

	public void setTitleLinkTarget(String titleLinkTarget) {
		this.titleLinkTarget = titleLinkTarget;
	}

	@Override
	public boolean isAllowEmptyImg() {
		return allowEmptyImg;
	}

	public void setAllowEmptyImg(boolean allowEmptyImg) {
		this.allowEmptyImg = allowEmptyImg;
	}

	public boolean isNaviButton() {
		return naviButton;
	}

	public boolean getNaviButton() {
		return naviButton;
	}

	public void setNaviButton(boolean paging) {
		this.naviButton = paging;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public boolean isPlaceHolder() {
		return getPlaceHolder();
	}

	public boolean getPlaceHolder() {
		String v = get("placeHolder");
		return "1".equals(v);
	}

	public void setPlaceHolder(String value) {
		this.set("placeHolder", value);
	}

	public boolean isFillBlank() {
		return getFillBlank();
	}

	public boolean getFillBlank() {
		String v = get("fillBlank");
		if (Str.isEmpty(v))
			v = "1";

		return "1".equals(v);
	}

	public void setFillBlank(String value) {
		this.set("fillBlank", value);
	}

	public boolean isShowMoreBtn() {
		return getShowMoreBtn();
	}

	public boolean getShowMoreBtn() {
		String v = get("showMoreBtn");
		return "1".equals(v);
	}

	public void setShowMoreBtn(String value) {
		this.set("showMoreBtn", value);
	}

	public boolean isAutoCutImage() {
		return getAutoCutImage();
	}

	public boolean getAutoCutImage() {
		String v = get("autoCutImage");
		return "1".equals(v);
	}

	public void setAutoCutImage(String value) {
		this.set("autoCutImage", value);
	}

	public String getViewExpression() {
		return viewExpression;
	}

	public void setViewExpression(String viewExpression) {
		this.viewExpression = viewExpression;
	}

	public FakeSubSystem<StyleItem> getStyleItems() {
		return styleItems;
	}

	public void setStyleItems(FakeSubSystem<StyleItem> styleItems) {
		this.styleItems = styleItems;
	}

}
