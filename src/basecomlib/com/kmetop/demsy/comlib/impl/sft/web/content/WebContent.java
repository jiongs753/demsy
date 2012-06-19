package com.kmetop.demsy.comlib.impl.sft.web.content;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_INFO;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.impl.sft.dic.Dic;
import com.kmetop.demsy.comlib.web.IStatistic;
import com.kmetop.demsy.comlib.web.IWebContent;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.lang.Dates;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
@BzSys(name = "网站信息发布", code = IWebContent.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_INFO, buildin = true//
, actions = { @BzAct(name = "录入", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		// , @BzAct(name = "转换为产品信息", typeCode = TYPE_BZFORM_EDIT_N, mode =
		// "bu1", plugin =
		// "com.kmetop.demsy.plugins.web.ConvertInfoToProduct")//
		, @BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e") //
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
		, @BzAct(name = "审核", typeCode = TYPE_BZFORM_EDIT_N, mode = "status")//
		, @BzAct(jsonData = "CommonBizAction_orderby.data.js") //
		, @BzAct(name = "录入专题", typeCode = TYPE_BZFORM_NEW, mode = "c1")//
		, @BzAct(name = "变更栏目", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "catalog", gridOrder = 2), //
		@BzFld(name = "文章标题", property = "name", mode = "c:M c1:M e:M *:N v:S", cascadeMode = "catalog.type:99:NM", gridOrder = 1),//
		@BzFld(property = "typeCode"), //
		@BzFld(property = "author"), //
		@BzFld(property = "origin"), //
		@BzFld(property = "status", gridOrder = 5), //
		@BzFld(property = "linkPath"), //
		@BzFld(property = "refrence"), //
		@BzFld(property = "parent"), //
		@BzFld(property = "image"), //
		@BzFld(property = "keywords"), //
		@BzFld(name = "文章摘要", property = "desc", gridField = false, mode = "c:E c1:E e:E *:N v:S", cascadeMode = "typeCode:0,2:E"), //
		@BzFld(property = "content")

})//
		, @BzGrp(name = "其他设置", code = "properties"//
		, fields = { @BzFld(name = "评论次数", property = "commentNum", mode = "*:N v:S", gridOrder = 3), //
				@BzFld(name = "浏览次数", property = "clickNum", mode = "*:N v:S", gridOrder = 4), //
				@BzFld(name = "录入时间", property = "created", mode = "*:N v:S", gridOrder = 6, pattern = "yyyy-MM-dd HH:mm"), //
				@BzFld(name = "录入帐号", property = "createdBy", mode = "*:N v:S"), //
				@BzFld(name = "更新时间", property = "updated", mode = "*:N v:S", gridOrder = 7, pattern = "yyyy-MM-dd HH:mm"), //
				@BzFld(name = "更新帐号", property = "updatedBy", mode = "*:N v:S"), //
				@BzFld(name = "人工顺序", property = "orderby", mode = "*:N", gridOrder = 8) //
		}) //
}// end groups
)
public class WebContent extends SFTBizComponent implements IWebContent, IStatistic {
	// public static final int MASK_REDIRECT = 1;// 2^0
	//
	// public static final int MASK_IS_HOT = 2;// 2^1
	//
	// public static final int MASK_ESSENTIAL = 4;// 精华
	//
	// public static final int MASK_RECOMMEND = 8;// 推荐

	public static final int MASK_RESEARCH = 16;// 随文调查

	@ManyToOne
	@Prop("category")
	@BzFld(name = "所属栏目", groupBy = true, mode = "c:M c1:M e:M bu:E *:N v:S")
	protected WebContentCategory catalog;

	@ManyToOne
	@BzFld(name = "所属专题", disabledNavi = true, mode = "c1:M *:N v:S")
	protected WebContent parent;

	@BzFld(name = "类型", mode = "c:M c1:M e:M *:N v:S", disabledNavi = true, cascadeMode = "catalog.type:0,1:M", defalutValue = "{catalog.infoType}", options = "0:编辑器,2:超链接,99:推荐")
	protected Integer typeCode;// 信息类型

	/*
	 * 内容编辑
	 */
	@BzFld(name = "来源", mode = "c:E c1:E e:E *:N v:S", cascadeMode = "typeCode:0:E typeCode:2,99:N catalog.type:99:N", gridField = false)
	protected String origin;

	@BzFld(name = "作者", mode = "c:E c1:E e:E *:N v:S", cascadeMode = "typeCode:0:E typeCode:2,99:N catalog.type:99:N", gridField = false)
	protected String author;

	@BzFld(name = "内容", mode = "c:E c1:E e:E *:N v:S", cascadeMode = "typeCode:0:E typeCode:2,99:N catalog.type:99:N", gridField = false)
	protected RichText content;

	/*
	 * 网页链接
	 */
	@Column(length = 128)
	@Prop("filePath")
	@BzFld(name = "链接地址", mode = "c:E c1:E e:E *:N v:S", disabledNavi = true, cascadeMode = "typeCode:2:E typeCode:0,99:N", gridField = false)
	protected Upload linkPath;

	// @Column(length = 16)
	// @BzFld(name = "链接目标", disabledNavi = true, cascadeMode =
	// "typeCode:2:E typeCode:0,99:N", gridField = false, options =
	// "_blank:新窗口,_parent:父窗口")
	// private String linkTarget;

	/*
	 * 信息推荐
	 */
	@ManyToOne
	@BzFld(name = "推荐来源", mode = "c:E c1:E e:E *:N v:S", disabledNavi = true, cascadeMode = "typeCode:99:E", gridField = false)
	protected WebContent refrence;

	@Prop("relatedDate")
	protected Date date;// 日期

	@Prop("workflowStepStatus")
	@BzFld(name = "审核状态", mode = "status:M *:N v:S", options = "{WebContent.status}")
	protected int status = 0;// 审核状态

	@Column(length = 256)
	@BzFld(name = "关键字", mode = "c:E c1:E e:E *:N v:S", gridField = false, cascadeMode = "typeCode:0:E catalog.type:99:N")
	private String keywords; // 关键字——用于搜索引擎进行网页搜索

	@Prop("logo1Image")
	@BzFld(name = "徽标", mode = "c:E c1:E e:E *:N v:S", uploadType = "*.bmp;*.jpg;*.gif;*.png;*.swf", gridField = false)
	protected Upload logo;

	@Prop("logo2Image")
	@BzFld(name = "图片", mode = "c:E c1:E e:E *:N v:S", uploadType = "*.bmp;*.jpg;*.gif;*.png;*.swf;*.flv", cascadeMode = "catalog.infoRequiredImage:1:M catalog.infoRequiredImage:0:E typeCode:99:N", gridField = false)
	protected Upload image;

	// 统计信息
	protected int commentNum;

	protected int clickNum;

	/*
	 * 老系统字段
	 */
	@Column(columnDefinition = "text")
	private String publishToStr;

	@ManyToOne
	protected Dic type;

	// =======================================================================
	// 接口实现
	// =======================================================================

	@Override
	public IWebContentCatalog getInfoCatalog() {
		if (refrence != null)
			return refrence.getInfoCatalog();

		return catalog;
	}

	@Override
	public Long getInfoID() {
		if (refrence != null)
			return refrence.getInfoID();

		return id;
	}

	@Override
	public String getInfoName() {
		if (refrence != null)
			return refrence.getInfoName();

		return name;
	}

	@Override
	public String getInfoAuthor() {
		if (refrence != null)
			return refrence.getInfoAuthor();

		return author;
	}

	@Override
	public String getInfoOrigin() {
		if (refrence != null)
			return refrence.getInfoOrigin();

		return origin;
	}

	@Override
	public RichText getInfoContent() {
		if (refrence != null) {
			return getRefrence().getInfoContent();
		}

		if (content == null || Str.isEmpty(content.toString())) {
			return null;
		}

		return content;
	}

	@Override
	public String getInfoLinkPath() {
		if (refrence != null)
			return refrence.getInfoLinkPath();

		return linkPath == null ? null : linkPath.toString();
	}

	// @Override
	// public String getInfoLinkTarget() {
	// if (refrence != null)
	// return refrence.getInfoLinkTarget();
	//
	// // return linkTarget == null ? null : linkTarget.toString();
	// }

	@Override
	public String getInfoLogo() {
		if (refrence != null) {
			return refrence.getInfoLogo();
		}

		return logo == null ? null : logo.toString();
	}

	@Override
	public String getInfoImage() {
		if (refrence != null) {
			return refrence.getInfoImage();
		}

		return image == null ? null : image.toString();
	}

	@Override
	public String getInfoDate() {
		if (refrence != null) {
			return refrence.getInfoDate();
		}

		return Dates.formatDateTime(date == null ? updated : date);
	}

	@Override
	public String getInfoDesc() {
		if (refrence != null) {
			return refrence.getInfoDesc();
		}

		if (Str.isEmpty(this.desc)) {
			String str = this.content == null ? "" : content.toString();
			return Str.substr(Str.escapeHTML(str).trim(), 50);
		}

		return Str.substr(Str.escapeHTML(desc).trim(), 50);
	}

	// =======================================================================
	// 以下： GETTER/SETTER 方法
	// =======================================================================

	public WebContent getParent() {
		return parent;
	}

	public void setParent(WebContent parent) {
		this.parent = parent;
	}

	public void setEnabledResearch(boolean flag) {
		super.setMask(MASK_RESEARCH, flag);
	}

	public boolean isEnabledResearch() {
		return super.getMask(MASK_RESEARCH);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int s) {
		this.status = s;
	}

	public Upload getLinkPath() {
		return linkPath;
	}

	public void setLinkPath(Upload filePath) {
		this.linkPath = filePath;
	}

	public WebContentCategory getCatalog() {
		return catalog;
	}

	public void setCatalog(WebContentCategory category) {
		this.catalog = category;
	}

	public RichText getContent() {
		if (content == null || Str.isEmpty(content.toString())) {
			return null;
		}
		return content;
	}

	public void setContent(RichText content) {
		this.content = content;
	}

	public Dic getType() {
		return type;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Upload getLogo() {
		return logo;
	}

	public void setLogo(Upload logo1Image) {
		this.logo = logo1Image;
	}

	public Upload getImage() {
		return image;
	}

	public void setImage(Upload logo2Image) {
		this.image = logo2Image;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentsCount) {
		this.commentNum = commentsCount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date dateTime) {
		this.date = dateTime;
	}

	public String getPublishToStr() {
		return publishToStr;
	}

	public void setPublishToStr(String publishToStr) {
		this.publishToStr = publishToStr;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public WebContent getRefrence() {
		return refrence;
	}

	public void setRefrence(WebContent refrence) {
		this.refrence = refrence;
	}

	//
	// public String getLinkTarget() {
	// return linkTarget;
	// }
	//
	// public void setLinkTarget(String linkTarget) {
	// this.linkTarget = linkTarget;
	// }

	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}
}
