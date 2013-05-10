package com.kmetop.demsy.comlib.impl.sft.web.content;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_INFO_CATALOG;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.impl.sft.system.AbstractSystemData;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "网站栏目设置", code = IWebContentCatalog.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_INFO_CATALOG, buildin = true//
, actions = { @BzAct(name = "新增栏目", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(name = "调整上级栏目", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(property = "parent", gridOrder = 3) //
		, @BzFld(name = "栏目名称", property = "name", mode = "c:M e:M", tostring = true, gridOrder = 1)//
		, @BzFld(name = "栏目编码", property = "code", mode = "c:M e:M", gridOrder = 2) //
		, @BzFld(property = "type", gridOrder = 4) //
		, @BzFld(property = "refrence") //
		, @BzFld(property = "infoType") //
		, @BzFld(property = "infoRequiredImage") //
		, @BzFld(property = "infoRequiredSumm") //
		, @BzFld(property = "infoEnabledSearch") //
		, @BzFld(property = "infoBatchInput") //
		, @BzFld(property = "image") //
		, @BzFld(property = "disabled") //
		, @BzFld(property = "customFields") //
		, @BzFld(property = "commentNum") //
		, @BzFld(property = "clickNum") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:N v:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:N v:P", gridField = false) //
		, @BzFld(name = "人工顺序", property = "orderby", mode = "*:N v:P", gridField = false) //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:N v:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:P", gridField = false) //

}) //
}// end groups
)
public class WebContentCategory extends SFTBizComponent implements IWebContentCatalog {

	@ManyToOne
	@BzFld(name = "上级栏目", mode = "bu:E")
	private WebContentCategory parent; // 上级栏目

	@BzFld(name = "栏目类型", disabledNavi = true, mode = "c:M e:M", options = "0:信息栏目,1:引用栏目,99:栏目分类")
	private Integer type;

	@Prop("restrictionWebContentType")
	@BzFld(name = "信息发布", gridField = false, disabledNavi = true, options = "0:内容编辑,2:网页链接", cascadeMode = "type:0,1:M")
	private Integer infoType;

	@BzFld(name = "栏目状态", gridField = false, disabledNavi = true, options = "1:停用,0:启用", mode = "c:N e:E")
	private Boolean disabled;

	@BzFld(name = "栏目隐藏", gridField = false, disabledNavi = true, options = "1:隐藏,0:显示")
	private Boolean hidden;

	@Prop("logoImage")
	@BzFld(name = "栏目图片", gridField = false, uploadType = "*.jpg;*.gif;*.png")
	private Upload image;

	@Prop("titleImage")
	@BzFld(name = "栏目徽标", gridField = false, uploadType = "*.jpg;*.gif;*.png")
	private Upload logo;

	@Prop("staticHrefUrl")
	@BzFld(name = "链接地址", gridField = false)
	private String linkPath;

	@Column(name = "_target", length = 16)
	@BzFld(name = "链接目标", gridField = false, disabledNavi = true, options = "_blank:新窗口,_self:本窗口,_parent:父窗口")
	private String linkTarget;

	/*
	 * 信息栏目
	 */
	@BzFld(name = "动态标题", gridField = false, disabledNavi = true, options = "1:动态,0:静态", cascadeMode = "type:0,1:E")
	private Boolean dynaName;

	@BzFld(name = "信息图片", gridField = false, disabledNavi = true, options = "1:必需,0:不需要", cascadeMode = "type:0,1:E")
	private Boolean infoRequiredImage;

	@BzFld(name = "信息摘要", gridField = false, disabledNavi = true, options = "1:必需,0:不需要", cascadeMode = "type:0,1:E")
	private Boolean infoRequiredSumm;

	@BzFld(name = "信息检索", gridField = false, disabledNavi = true, options = "0:禁用,1:启用", cascadeMode = "type:0,1:E")
	private Boolean infoEnabledSearch;

	@BzFld(name = "批量录入", gridField = false, disabledNavi = true, options = "1:启用,0:禁用", cascadeMode = "type:0,1:E")
	@Prop("imageLib")
	private Boolean infoBatchInput;

	// 统计信息
	@BzFld(name = "评论次数", mode = "*:N v:P")
	protected int commentNum;

	@BzFld(name = "点击次数", mode = "*:N v:P")
	protected int clickNum;

	/*
	 * 引用栏目
	 */
	@ManyToOne
	@BzFld(name = "栏目引用", disabledNavi = true, cascadeMode = "type:1:E", gridField = false)
	private WebContentCategory refrence;

	/*
	 * 文件夹
	 */
	@BzFld(name = "自定义栏目", gridField = false, disabledNavi = true, options = "1:启用,0:禁用", cascadeMode = "type:99:E")
	private Boolean enabledCustom;

	/*
	 * 老系统字段
	 */
	@OneToOne
	private WebContentCategoryResource resource; // 栏目功能模块

	@BzFld(name = "字段设置", gridField = false, refrenceFields = "propName,name,mode,type")
	protected FakeSubSystem<AbstractSystemData> customFields;

	// =======================================================================
	// 接口实现
	// =======================================================================
	public Long getCatalogID() {
		if (refrence != null) {
			return refrence.getCatalogID();
		}

		return id;
	}

	public String getCatalogName() {
		if (refrence != null) {
			return refrence.getCatalogName();
		}

		return name;
	}

	// =======================================================================
	// 以下： GETTER/SETTER 方法
	// =======================================================================
	public void setDisabledModule(boolean flag) {
		super.set((byte) 2, flag);
	}

	public boolean isDisabledModule() {
		return super.is((byte) 2);
	}

	public void setEnabledCustom(boolean flag) {
		enabledCustom = flag;
	}

	public boolean isEnabledCustom() {
		if (enabledCustom == null)
			return super.is((byte) 7);

		return enabledCustom;
	}

	public void setHidden(boolean flag) {
		hidden = flag;
	}

	public boolean isHidden() {
		if (hidden == null)
			return super.is((byte) 4);

		return hidden;
	}

	public void setInfoRequiredSumm(boolean flag) {
		infoRequiredSumm = flag;
	}

	public boolean isInfoRequiredSumm() {
		if (infoRequiredSumm == null)
			return super.is((byte) 9);

		return infoRequiredSumm;
	}

	public void setInfoRequiredImage(boolean flag) {
		infoRequiredImage = flag;
	}

	public boolean isInfoRequiredImage() {
		if (infoRequiredImage == null)
			return super.is((byte) 8);

		return infoRequiredImage;
	}

	public void setInfoEnabledSearch(boolean flag) {
		infoEnabledSearch = flag;
	}

	public boolean getInfoEnabledSearch() {
		return infoEnabledSearch;
	}

	public WebContentCategory getParent() {
		return parent;
	}

	public void setParent(WebContentCategory parent) {
		this.parent = parent;
	}

	public String getLinkPath() {
		return linkPath;
	}

	public void setLinkPath(String staticHrefUrl) {
		this.linkPath = staticHrefUrl;
	}

	public String getLinkTarget() {
		return linkTarget;
	}

	public void setLinkTarget(String target) {
		this.linkTarget = target;
	}

	public Upload getLogo() {
		return logo;
	}

	public void setLogo(Upload titleImage) {
		this.logo = titleImage;
	}

	public Upload getImage() {
		return image;
	}

	public void setImage(Upload logoImage) {
		this.image = logoImage;
	}

	public boolean isImageLib() {
		if (infoBatchInput == null)
			return super.is((byte) 10);

		return infoBatchInput;
	}

	public void setImageLib(boolean flag) {
		this.infoBatchInput = flag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getInfoType() {
		return infoType;
	}

	public void setInfoType(Integer infoPublishType) {
		this.infoType = infoPublishType;
	}

	public boolean isDynaName() {
		if (dynaName == null)
			return super.is((byte) 11);

		return dynaName;
	}

	public boolean isInfoDisabled() {
		return super.is((byte) 1);
	}

	public void setDynaName(boolean flag) {
		dynaName = flag;
	}

	public WebContentCategoryResource getResource() {
		return resource;
	}

	public void setResource(WebContentCategoryResource resource) {
		this.resource = resource;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public WebContentCategory getRefrence() {
		return refrence;
	}

	public void setRefrence(WebContentCategory refrence) {
		this.refrence = refrence;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	@Override
	public FakeSubSystem getCustomFields() {
		return customFields;
	}

	public void setCustomFields(FakeSubSystem customFields) {
		this.customFields = customFields;
	}
}
