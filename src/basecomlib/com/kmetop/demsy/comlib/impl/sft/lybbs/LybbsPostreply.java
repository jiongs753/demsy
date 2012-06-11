package com.kmetop.demsy.comlib.impl.sft.lybbs;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_FORUM_REPLY;

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
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.comlib.web.IBbsReply;
import com.kmetop.demsy.lang.Str;

@Entity
@Table(name = "lybbs_postreply")
@BzSys(name = "论坛回帖管理", code = IBbsReply.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_FORUM_REPLY//
, actions = {
//
		@BzAct(name = "回复", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.bbs.SaveBbsReply", info = "回帖成功!", error = "回帖失败!")//
		, @BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e") //
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
		, @BzAct(name = "审核", typeCode = TYPE_BZFORM_EDIT_N, mode = "audit") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {//
@BzFld(property = "forum", gridOrder = 1)//
		, @BzFld(property = "topic")//
		, @BzFld(property = "name", gridOrder = 2)//
		, @BzFld(property = "catalog") //
		, @BzFld(property = "content", gridOrder = 3) //
		, @BzFld(property = "showsign") //
		, @BzFld(property = "viewMode", gridOrder = 8) //
		, @BzFld(property = "viewUsers") //
		, @BzFld(property = "created", gridOrder = 4) //
		, @BzFld(property = "createdBy", gridOrder = 5) //
		, @BzFld(property = "createdIP", gridOrder = 6) //
// , @BzFld(property = "updatedBy") //
// , @BzFld(property = "updatedIP") //
// , @BzFld(property = "updated") //
}), @BzGrp(name = "帖子状态", code = "status"//
, fields = {//
@BzFld(property = "status", gridOrder = 7) //
}) }// end groups
)
public class LybbsPostreply implements IBbsReply {
	@Id
	@GeneratedValue(generator = "SftIdGen", strategy = GenerationType.TABLE)
	@TableGenerator(name = "SftIdGen", table = "DEMSY_00000000", pkColumnName = "id_key", valueColumnName = "next_hi", allocationSize = 1, initialValue = 20)
	protected Integer id;

	/*
	 * 基本信息
	 */
	@Column(length = 20, name = "titleicon")
	@BzFld(name = "回复分类", mode = "c:E e:E v:S *:N", options = "em45.gif:不解,em46.gif:靠,em47.gif:大汗,em48.gif:傻了,em49.gif:开心,em50.gif:哭,em51.gif:欠揍,em52.gif:必胜,em53.gif:支持,em54.gif:收到,em55.gif:讨厌,em56.gif:怕怕", disabledNavi = true)
	protected String catalog;

	@Column(columnDefinition = "text")
	@BzFld(name = "回复内容", mode = "c:E e:E v:S *:N")
	protected RichText content;

	@BzFld(name = "显示签名", mode = "c:E v:S *:N", disabledNavi = true, options = "1:显示,0:隐藏")
	protected boolean showsign = true;

	@Column(name = "viewmode")
	@BzFld(name = "查看方式", mode = "c:E e:E v:S *:N", options = "0:所有用户,1:登录用户,3:版主查看,4:指定用户")
	protected byte viewMode;// 2:回复查看,5:经验查看,6:威望查看,7:购买查看

	@Column(name = "viewusers")
	@BzFld(name = "指定用户", mode = "c:E e:E v:S *:N")
	protected String viewUsers;

	/*
	 * 自动生成
	 */
	@ManyToOne
	@Column(name = "forumid")
	@BzFld(name = "所属论坛", mode = "v:S *:N")
	protected LybbsDb forum;

	@ManyToOne
	@Column(name = "topicid")
	@BzFld(name = "帖子主题", mode = "c:SM e:SM v:S *:N", disabledNavi = true)
	protected LybbsPosttopic topic;

	@Column(length = 90, name = "title")
	@BzFld(name = "回复标题", mode = "v:S *:N")
	protected String name;

	@Column(length = 40, name = "author")
	@BzFld(name = "回复作者", mode = "v:S *:N")
	protected String createdBy;

	@Column(name = "postattime")
	@BzFld(name = "回复时间", mode = "*:N v:S")
	protected Date created;

	@Column(length = 64, name = "postip")
	@BzFld(name = "IP地址", mode = "*:N v:S")
	protected String createdIP;

	@Column(length = 40, name = "lasteditauthor")
	@BzFld(name = "最近修改帐号", mode = "*:N v:S")
	protected String updatedBy;

	@Column(name = "lasteditat")
	@BzFld(name = "最近修改时间", mode = "*:N v:S")
	protected Integer updated;

	@Column(length = 64, name = "lasteditip")
	@BzFld(name = "最近修改IP", mode = "*:N v:S")
	protected String updatedIP;

	/*
	 * 版主设置
	 */
	@Column(name = "hide")
	@BzFld(name = "审核状态", mode = "*:N v:S audit:E", options = "0:未审核,1:屏蔽,2:显示")
	protected byte status;

	@Transient
	protected IUser author;

	@Transient
	private String warning;

	public String toString() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String title) {
		this.name = title;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String titleicon) {
		this.catalog = titleicon;
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

	public boolean isShowsign() {
		return showsign;
	}

	public void setShowsign(boolean showsign) {
		this.showsign = showsign;
	}

	public byte getViewMode() {
		return viewMode;
	}

	public void setViewMode(byte viewmode) {
		this.viewMode = viewmode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String author) {
		this.createdBy = author;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date postattime) {
		this.created = postattime;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String postip) {
		this.createdIP = postip;
	}

	// public String getLasteditauthor() {
	// return lasteditauthor;
	// }
	//
	// public void setLasteditauthor(String lasteditauthor) {
	// this.lasteditauthor = lasteditauthor;
	// }
	//
	// public Integer getLasteditat() {
	// return lasteditat;
	// }
	//
	// public void setLasteditat(Integer lasteditat) {
	// this.lasteditat = lasteditat;
	// }
	//
	// public String getLasteditip() {
	// return lasteditip;
	// }
	//
	// public void setLasteditip(String lasteditip) {
	// this.lasteditip = lasteditip;
	// }

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte hide) {
		this.status = hide;
	}

	public LybbsDb getForum() {
		return forum;
	}

	public void setForum(LybbsDb forum) {
		this.forum = forum;
	}

	public LybbsPosttopic getTopic() {
		return topic;
	}

	public void setTopic(LybbsPosttopic topic) {
		this.topic = topic;
	}

	public IUser getAuthor() {
		return author;
	}

	public void setAuthor(IUser user) {
		this.author = user;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getUpdated() {
		return updated;
	}

	public void setUpdated(Integer updated) {
		this.updated = updated;
	}

	public void setUpdated(Date updated) {
		this.updated = (int) (updated.getTime() / 1000);
	}

	public String getUpdatedIP() {
		return updatedIP;
	}

	public void setUpdatedIP(String updatedIP) {
		this.updatedIP = updatedIP;
	}

	public String getViewUsers() {
		return viewUsers;
	}

	public void setViewUsers(String viewusers) {
		this.viewUsers = viewusers;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	@Override
	public String getDesc() {
		String str = this.content == null ? "" : content.toString();
		return Str.substr(Str.escapeHTML(str).trim(), 50);
	}
	// protected Integer postat;
	// protected Integer authorid;
	// protected boolean accessary;

	// protected Short invisible;
	// protected Integer viewnumber;

	// protected Integer lasteditauthorid;
	// protected Short lastscore;

	// protected Integer lastscoreauthorid;

	// @Column(length = 40)
	// protected String lastscoreauthor;

	// protected Integer lastscoreat;

}