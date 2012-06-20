package com.kmetop.demsy.comlib.impl.base.lib;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_DEMSY_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_LIB_UIMODEL;
import static com.kmetop.demsy.comlib.LibConst.ORDER_DEMSY_LIB_UI_MODEL;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.IRuntimeConfigable;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.impl.sft.system.AbstractSystemData;
import com.kmetop.demsy.comlib.ui.IUIViewComponent;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "视图组件库", code = BIZSYS_DEMSY_LIB_UIMODEL, catalog = BIZCATA_DEMSY_ADMIN, orderby = ORDER_DEMSY_LIB_UI_MODEL//
, actions = { @BzAct(name = "新增", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "name", name = "名称", mode = "c:M e:M")//
		, @BzFld(property = "code", name = "编号", mode = "c:M e:M")//
		, @BzFld(property = "parent") //
		, @BzFld(property = "type") //
		, @BzFld(property = "image") //
		, @BzFld(property = "defaultWidth") //
		, @BzFld(property = "defaultHeight") //
		, @BzFld(property = "viewTemplate") //
		, @BzFld(property = "viewExpression") //
		, @BzFld(property = "viewController") //
		, @BzFld(property = "created", name = "创建时间", mode = "*:P") //
		, @BzFld(property = "updated", name = "更新时间", mode = "*:P") //
}) }// end groups
, jsonData = "UIViewComponent.data.js"//
)
public class UiModelLib extends BizComponent implements IUIViewComponent, IRuntimeConfigable {

	@ManyToOne
	@BzFld(name = "上级分类", options = "['type in 1']")
	protected UiModelLib parent;

	@BzFld(name = "构件类型", options = "0:板块,1:分类", mode = "c:M e:M", disabledNavi = true)
	protected byte type;

	@Column(length = 64)
	@BzFld(name = "视图模板", cascadeMode = "type:0:M")
	@Prop("template")
	protected String viewTemplate;

	@Column(length = 512)
	@BzFld(name = "视图表达式", cascadeMode = "type:0:M")
	protected String viewExpression;

	@Column(length = 256)
	@BzFld(name = "视图控制器", cascadeMode = "type:0:M")
	@Prop("dataSource")
	protected String viewController;

	@BzFld(name = "效果图", uploadType = "*.jpg;*.gif;*.png")
	protected Upload image;

	@BzFld(name = "默认宽度")
	protected int defaultWidth;

	@BzFld(name = "默认高度")
	protected int defaultHeight;

	@BzFld(name = "字段设置", gridField = false, refrenceFields = "propName,name,mode,type")
	protected FakeSubSystem<AbstractSystemData> customFields;

	@BzFld(name = "标题选项", options = "0:关闭,1:打开")
	private boolean titleOptions;

	@BzFld(name = "图片选项", options = "0:关闭,1:打开")
	private boolean imageOptions;

	@BzFld(name = "摘要选项", options = "0:关闭,1:打开")
	private boolean summOptions;

	@BzFld(name = "滚动选项", options = "0:关闭,1:打开")
	private boolean scrollOptions;

	@OneToMany(mappedBy = "parent")
	protected List<UiModelLib> children;

	public UiModelLib getParent() {
		return parent;
	}

	public void setParent(UiModelLib parent) {
		this.parent = parent;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getViewTemplate() {
		return viewTemplate;
	}

	public List<UiModelLib> getChildren() {
		return children;
	}

	public void setChildren(List<UiModelLib> children) {
		this.children = children;
	}

	public Upload getImage() {
		return image;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public String getViewController() {
		return viewController;
	}

	public String getViewExpression() {
		return viewExpression;
	}

	public void setViewExpression(String viewExpression) {
		this.viewExpression = viewExpression;
	}

	public void setViewTemplate(String viewTemplate) {
		this.viewTemplate = viewTemplate;
	}

	public void setViewController(String viewController) {
		this.viewController = viewController;
	}

	public FakeSubSystem<AbstractSystemData> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(FakeSubSystem<AbstractSystemData> customFields) {
		this.customFields = customFields;
	}

	public boolean isTitleOptions() {
		return titleOptions;
	}

	public void setTitleOptions(boolean titleOptions) {
		this.titleOptions = titleOptions;
	}

	public boolean isImageOptions() {
		return imageOptions;
	}

	public void setImageOptions(boolean imageOptions) {
		this.imageOptions = imageOptions;
	}

	public boolean isSummOptions() {
		return summOptions;
	}

	public void setSummOptions(boolean summOptions) {
		this.summOptions = summOptions;
	}

	public boolean isScrollOptions() {
		return scrollOptions;
	}

	public void setScrollOptions(boolean scrollOptions) {
		this.scrollOptions = scrollOptions;
	}

	public int getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public int getDefaultHeight() {
		return defaultHeight;
	}

	public void setDefaultHeight(int defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

}
