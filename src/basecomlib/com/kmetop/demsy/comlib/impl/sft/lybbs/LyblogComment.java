package com.kmetop.demsy.comlib.impl.sft.lybbs;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_BLOG_COMMENT;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.web.IBlogPostComment;
import com.kmetop.demsy.lang.Str;

@Entity
@Table(name = "lyblog_comment")
@BzSys(name = "博客文章留言", code = IBlogPostComment.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_BLOG_COMMENT//
, actions = {
//
		@BzAct(name = "评论", typeCode = TYPE_BZFORM_NEW, mode = "c",plugin = "com.kmetop.demsy.plugins.bbs.SaveBlogComment")//
		, @BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e") //
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
		, @BzAct(name = "屏蔽", typeCode = TYPE_BZFORM_EDIT_N, mode = "hide") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(property = "name", gridOrder = 1)//
		, @BzFld(property = "post") //
		, @BzFld(property = "content", gridOrder = 2) //
		, @BzFld(property = "created", gridOrder = 3) //
		, @BzFld(property = "createdBy", gridOrder = 4) //
		, @BzFld(property = "createdIP", gridOrder = 7) //
// , @BzFld(property = "hide", gridOrder = 9) //
}) }// end groups
)
public class LyblogComment implements IBlogPostComment {
	@Id
	@GeneratedValue(generator = "SftIdGen", strategy = GenerationType.TABLE)
	@TableGenerator(name = "SftIdGen", table = "DEMSY_00000000", pkColumnName = "id_key", valueColumnName = "next_hi", allocationSize = 1, initialValue = 20)
	protected Integer id;

	/*
	 * 基本信息
	 */

	// protected Integer userid;

	@Column(name = "username", length = 40)
	@BzFld(name = "用户帐号", mode = "v:S *:N")
	protected String createdBy;

	// protected Integer bloguserid;

	// protected Integer groupid;

	@ManyToOne
	@Column(name = "postid")
	@BzFld(name = "博客文章", mode = "c:HM e:HM v:S *:N", disabledNavi = true)
	protected LyblogPosts post;

	@Column(length = 4000)
	@BzFld(name = "评论内容", mode = "c:M e:M v:S *:N")
	protected RichText content;

	/*
	 * 自动生成
	 */
	@Column(name = "title", length = 200)
	@BzFld(name = "博文标题", mode = "v:S *:N")
	protected String name;

	protected long postat;

	@Transient
	@BzFld(name = "评论时间", mode = "v:S *:N", isTransient = true)
	protected Date created;

	@Column(name = "postip", length = 64)
	@BzFld(name = "IP地址", mode = "v:S *:N")
	protected String createdIP;

	// @BzFld(name = "是否屏蔽", mode = "hide:E v:S *:N", options = "1:屏蔽,0:显示")
	// protected byte hide;

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

	public LyblogPosts getPost() {
		return post;
	}

	public void setPost(LyblogPosts post) {
		this.post = post;
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

	public String getName() {
		return name;
	}

	public void setName(String title) {
		this.name = title;
	}

	public Long getPostat() {
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

	// public byte isHide() {
	// return hide;
	// }
	//
	// public void setHide(byte hide) {
	// this.hide = hide;
	// }

	public void setCreated(Date created) {
		this.created = created;
		if (created != null)
			this.postat = created.getTime();
	}

}