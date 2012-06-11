package com.kmetop.demsy.comlib.impl.sft.lybbs;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_BLOG_TOPIC;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.web.IBlogPost;
import com.kmetop.demsy.comlib.web.IStatistic;
import com.kmetop.demsy.lang.Str;

@Entity
@Table(name = "lyblog_posts")
@BzSys(name = "博客文章管理", code = IBlogPost.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_BLOG_TOPIC//
, actions = {
//
		@BzAct(name = "写博文", typeCode = TYPE_BZFORM_NEW, mode = "c",plugin = "com.kmetop.demsy.plugins.bbs.SaveBlogPost")//
		, @BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e") //
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
		, @BzAct(name = "屏蔽", typeCode = TYPE_BZFORM_EDIT_N, mode = "hide")//
		, @BzAct(name = "推荐", typeCode = TYPE_BZFORM_EDIT_N, mode = "elite") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(property = "name", gridOrder = 1)//
		, @BzFld(property = "desc", gridOrder = 2) //
		, @BzFld(property = "content") //
		, @BzFld(property = "created", gridOrder = 3) //
		, @BzFld(property = "createdBy", gridOrder = 4) //
		, @BzFld(property = "commentNum", gridOrder = 5) //
		, @BzFld(property = "clickNum", gridOrder = 6) //
		, @BzFld(property = "createdIP", gridOrder = 7) //
		, @BzFld(property = "elite", gridOrder = 8) //
		, @BzFld(property = "hide", gridOrder = 9) //
}) }// end groups
)
public class LyblogPosts implements IBlogPost, IStatistic {
	@Id
	@GeneratedValue(generator = "SftIdGen", strategy = GenerationType.TABLE)
	@TableGenerator(name = "SftIdGen", table = "DEMSY_00000000", pkColumnName = "id_key", valueColumnName = "next_hi", allocationSize = 1, initialValue = 20)
	protected Integer id;

	/*
	 * 基本信息
	 */
	// protected Integer userid;

	@Column(name = "username", length = 40)
	@BzFld(name = "博文作者", mode = "v:S *:N")
	protected String createdBy;

	// protected Integer blogid;

	// protected Integer catalogid;

	// @Column(length = 100)
	// protected String catalogname;

	@Column(name = "title", length = 200)
	@BzFld(name = "博文标题", mode = "c:M e:M v:S *:N")
	protected String name;

	@Column(name = "summary", length = 4000)
	@BzFld(name = "博文摘要", mode = "c:M e:M v:S *:N")
	protected String desc;

	@Column(columnDefinition = "text")
	@BzFld(name = "博文内容", mode = "c:M e:M v:S *:N")
	protected RichText content;

	/*
	 * 自动生成
	 */
	protected long postat;

	@Transient
	@BzFld(name = "发表时间", mode = "v:S *:N", isTransient = true)
	protected Date created;

	@Column(name = "postip", length = 64)
	@BzFld(name = "IP地址", mode = "v:S *:N")
	protected String createdIP;

	@Column(name = "clicknum")
	@BzFld(name = "点击次数", mode = "*:N v:S")
	protected Integer clickNum;

	@Column(name = "commentnum")
	@BzFld(name = "回复次数", mode = "*:N v:S")
	protected Integer commentNum;

	//
	// @Column(name = "longlastclickat")
	// @BzFld(name = "最近点击", mode = "v:S *:N")
	// protected long lastClickAt;

	// protected double clickFrequency;

	/*
	 * 管理员设置
	 */
	@Column(name = "recommend")
	@BzFld(name = "是否推荐", mode = "elite:E v:S *:N", options = "1:推荐,0:未推荐")
	protected boolean elite;

	// @BzFld(name = "推荐时间", mode = "v:S *:N")
	// @Column(name = "longrecommendat")
	// protected long eliteAt;

	// protected byte essential;
	//
	// protected long longEssentialAt;

	@BzFld(name = "是否屏蔽", mode = "hide:E v:S *:N", options = "1:屏蔽,0:显示")
	protected boolean hide;

	// @Column(name = "longhideat")
	// @BzFld(name = "屏蔽时间", mode = "v:S *:N")
	// protected long hideAt;

	public String toString() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String username) {
		this.createdBy = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String title) {
		this.name = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String summary) {
		this.desc = summary;
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

	public long getPostat() {
		return postat;
	}

	public Date getCreated() {
		return created;
	}

	public void setPostat(long postat) {
		this.postat = postat;
		created = new Date(postat);
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String postip) {
		this.createdIP = postip;
	}

	//
	// public long getLastClickAt() {
	// return lastClickAt;
	// }
	//
	// public void setLastClickAt(long lastClickAt) {
	// this.lastClickAt = lastClickAt;
	// }

	public boolean isElite() {
		return elite;
	}

	public void setElite(boolean elite) {
		this.elite = elite;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public void setCreated(Date created) {
		this.created = created;
		if (created != null)
			this.postat = created.getTime();
	}

}