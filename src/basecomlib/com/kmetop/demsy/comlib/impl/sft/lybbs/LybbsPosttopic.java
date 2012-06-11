package com.kmetop.demsy.comlib.impl.sft.lybbs;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_WEB;
import static com.kmetop.demsy.comlib.LibConst.ORDER_WEB_FORUM_TOPIC;

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
import com.kmetop.demsy.comlib.web.IBbsTopic;
import com.kmetop.demsy.comlib.web.IStatistic;
import com.kmetop.demsy.lang.Str;

@Entity
@Table(name = "lybbs_posttopic")
@BzSys(name = "论坛发帖管理", code = IBbsTopic.SYS_CODE, catalog = BIZCATA_WEB, orderby = ORDER_WEB_FORUM_TOPIC//
, actions = {
//
		@BzAct(name = "发帖", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.bbs.SaveBbsTopic", info = "发帖成功!", error = "发帖失败!")//
		, @BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e") //
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
		, @BzAct(name = "审核", typeCode = TYPE_BZFORM_EDIT_N, mode = "audit")//
		, @BzAct(name = "移动", typeCode = TYPE_BZFORM_EDIT_N, mode = "move") //
// , @BzAct(name = "配色", typeCode = TYPE_BZFORM_EDIT_N, mode = "color")//
// , @BzAct(name = "固定", typeCode = TYPE_BZFORM_EDIT_N, mode = "locktop")//
// , @BzAct(name = "锁定", typeCode = TYPE_BZFORM_EDIT_N, mode = "lockreply")//
// , @BzAct(name = "精华", typeCode = TYPE_BZFORM_EDIT_N, mode = "elite") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = {
//
		@BzFld(property = "forum", gridOrder = 1)//
		, @BzFld(property = "name", gridOrder = 2)//
		, @BzFld(property = "catalog") //
		, @BzFld(property = "content", gridOrder = 3) //
		, @BzFld(property = "showsign") //
		, @BzFld(property = "viewMode", gridOrder = 8) //
		, @BzFld(property = "viewUsers") //
		, @BzFld(property = "created", gridOrder = 4) //
		, @BzFld(property = "createdBy", gridOrder = 5) //
		, @BzFld(property = "createdIP") //
		// , @BzFld(property = "lasteditauthor") //
		// , @BzFld(property = "lasteditip") //
		// , @BzFld(property = "lasteditat") //
		, @BzFld(property = "clickNum", gridOrder = 6)//
		, @BzFld(property = "commentNum", gridOrder = 7) //
}), @BzGrp(name = "帖子状态", code = "status"//
, fields = {
//
// @BzFld(property = "locktop")//
// , @BzFld(property = "lockreply")//
@BzFld(property = "status", gridOrder = 8) //
// , @BzFld(property = "elite") //
// , @BzFld(property = "color") //
// , @BzFld(property = "boldcode") //
}) }// end groups
)
public class LybbsPosttopic implements IBbsTopic, IStatistic {
	@Id
	@GeneratedValue(generator = "SftIdGen", strategy = GenerationType.TABLE)
	@TableGenerator(name = "SftIdGen", table = "DEMSY_00000000", pkColumnName = "id_key", valueColumnName = "next_hi", allocationSize = 1, initialValue = 20)
	protected Integer id;

	/*
	 * 基本信息
	 */
	@ManyToOne
	@Column(name = "forumid")
	@BzFld(name = "所属论坛", mode = "c:M e:M v:S *:N move:E")
	protected LybbsDb forum;

	@Column(length = 80, name = "title")
	@BzFld(name = "帖子标题", mode = "c:M e:M v:S *:N")
	protected String name;

	@Column(length = 20, name = "titleicon")
	@BzFld(name = "帖子分类", mode = "c:E e:E v:S *:N", options = "em01.gif:原创,em02.gif:转帖,em03.gif:讨论,em04.gif:求助,em05.gif:推荐,em06.gif:公告,em07.gif:注意,em08.gif:贴图,em09.gif:建议,em10.gif:下载,em11.gif:灌水,em12.gif:分享", disabledNavi = true)
	protected String catalog;

	@Column(columnDefinition = "text")
	@BzFld(name = "帖子内容", mode = "c:M e:M v:S *:N")
	protected RichText content;

	@BzFld(name = "显示签名", mode = "v:S *:N", disabledNavi = true, options = "1:显示,0:隐藏")
	protected boolean showsign = true;

	@Column(name = "viewmode")
	@BzFld(name = "查看方式", mode = "c:E e:E v:S *:N", options = "0:所有用户,1:登录用户,3:版主查看,4:指定用户")
	protected byte viewMode;// 2:回复查看,,5:经验查看,6:威望查看,7:购买查看

	@Column(name = "viewusers")
	@BzFld(name = "指定用户", mode = "c:E e:E v:S *:N")
	protected String viewUsers;

	/*
	 * 自动生成
	 */

	@Column(name = "postattime")
	@BzFld(name = "发帖时间", mode = "*:N v:S")
	protected Date created;

	@Column(length = 40, name = "author")
	@BzFld(name = "帖子作者", mode = "c:H e:H v:S *:N")
	protected String createdBy;

	@Column(length = 64, name = "postip")
	@BzFld(name = "IP地址", mode = "*:N v:S")
	protected String createdIP;

	@Column(name = "clicktimes")
	@BzFld(name = "点击次数", mode = "*:N v:S")
	protected Integer clickNum;

	@Column(name = "replynum")
	@BzFld(name = "回复次数", mode = "*:N v:S")
	protected Integer commentNum;

	// @Column(length = 40)
	// @BzFld(name = "回复网友", mode = "*:N v:S")
	// protected String lasteditauthor;

	// @Column(length = 64)
	// @BzFld(name = "回复IP", mode = "*:N v:S")
	// protected String lasteditip;
	//
	// @BzFld(name = "回复时间", mode = "*:N v:S")
	// protected Integer lasteditat;
	//
	// @Transient
	// protected Date lasteditattime;

	/*
	 * 版主设置
	 */
	@BzFld(name = "是否固顶", mode = "*:N v:S locktop:E", options = "1:固顶,0:不固顶")
	protected byte locktop;

	@BzFld(name = "是否锁定", mode = "*:N v:S lockreply:E", options = "1:锁定,0:不锁定")
	protected byte lockreply;

	@Column(name = "hide")
	@BzFld(name = "审核状态", mode = "*:N v:S audit:E", options = "0:未审核,1:屏蔽,2:显示")
	protected byte status;

	@BzFld(name = "是否推荐", mode = "*:N v:S elite:E", options = "0:未推荐,1:精华,2:推荐")
	protected byte elite;

	@Column(length = 7)
	@BzFld(name = "颜色", mode = "*:N v:S color:E")
	protected String color;

	@BzFld(name = "字体", mode = "*:N v:S color:E", disabledNavi = true, options = "0:无,1:加粗,2:斜体,3:加粗+斜体,4:下划线,5:加粗+下划线,6:斜体+下划线,7:加粗+斜体+下划线")
	protected byte boldcode;

	@Transient
	protected IUser user;

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date postattime) {
		this.created = postattime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String author) {
		this.createdBy = author;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String postip) {
		this.createdIP = postip;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clicktimes) {
		this.clickNum = clicktimes;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer replynum) {
		this.commentNum = replynum;
	}

	public byte isLocktop() {
		return locktop;
	}

	public void setLocktop(byte locktop) {
		this.locktop = locktop;
	}

	public byte isLockreply() {
		return lockreply;
	}

	public void setLockreply(byte lockreply) {
		this.lockreply = lockreply;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte hide) {
		this.status = hide;
	}

	public byte getElite() {
		return elite;
	}

	public void setElite(byte elite) {
		this.elite = elite;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public byte getBoldcode() {
		return boldcode;
	}

	public void setBoldcode(byte boldcode) {
		this.boldcode = boldcode;
	}

	public LybbsDb getForum() {
		return forum;
	}

	public void setForum(LybbsDb forum) {
		this.forum = forum;
	}

	//
	// public String getLasteditauthor() {
	// return lasteditauthor;
	// }
	//
	// public void setLasteditauthor(String lasteditauthor) {
	// this.lasteditauthor = lasteditauthor;
	// }

	public String getPosticon() {
		String postIcon;
		postIcon = "topicnew0.gif";
		if (locktop > 0) {
			postIcon = locktop == (byte) 3 ? "lockalltop.gif"
					: locktop == (byte) 2 ? "lockcattop.gif" : "locktop.gif";
		} else {
			if (lockreply == (byte) 1)
				postIcon = "topiclocked0.gif";
			else if (commentNum > 0 && commentNum >= 10) {
				// 是否升级为热门帖
				// forumTopicFactory.setHotPost(postID);
				postIcon = "topichot0.gif";
			}
		}
		return postIcon;
	}

	public IUser getAuthor() {
		return user;
	}

	public void setAuthor(IUser user) {
		this.user = user;
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

	public void setWarning(String warn) {
		this.warning = warn;
	}

	@Override
	public String getDesc() {
		String str = this.content == null ? "" : content.toString();
		return Str.substr(Str.escapeHTML(str).trim(), 50);
	}

	// public String getLasteditip() {
	// return lasteditip;
	// }
	//
	// public void setLasteditip(String lasteditip) {
	// this.lasteditip = lasteditip;
	// }
	//
	// public Integer getLasteditat() {
	// return lasteditat;
	// }
	//
	// public void setLasteditat(Integer lasteditat) {
	// this.lasteditat = lasteditat;
	// if (lasteditat > 0)
	// this.lasteditattime = new Date((long) lasteditat * 1000);
	// }
	//
	// public Date getLasteditattime() {
	// return lasteditattime;
	// }

	/*
	 * 停用字段
	 */

	// @BzFld(name = "查看值")
	// protected Integer viewnumber;
	// protected Integer postat;
	// protected Short locktopid;
	// protected byte lockvote;
	// protected byte vote;
	// protected Integer votenum;
	// protected byte invisible;
	// protected byte accessary;
	// protected Integer lasteditauthorid;
	// protected byte lastscore;
	// protected Integer lastscoreauthorid;
	// @Column(length = 40)
	// protected String lastscoreauthor;
	// protected Integer lastscoreat;

}