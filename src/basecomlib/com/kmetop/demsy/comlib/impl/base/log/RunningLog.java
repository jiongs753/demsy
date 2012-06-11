package com.kmetop.demsy.comlib.impl.base.log;

import static com.kmetop.demsy.biz.BizConst.*;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_DEMSY_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.ORDER_DEMSY_LOG;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;

@Entity
@BzSys(name = "系统日志管理", code = "RunningLog", catalog = BIZCATA_DEMSY_ADMIN, orderby = ORDER_DEMSY_LOG//
, actions = {
//
		@BzAct(name = "清空", typeCode = TYPE_BZ_CLEAR, mode = "clr") //
		, @BzAct(name = "详情", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "datetime")//
		, @BzFld(property = "locationinfo") //
		, @BzFld(property = "message") //
		, @BzFld(property = "remoteIp")//
		, @BzFld(property = "remoteUri")//
		, @BzFld(property = "eslipse")//
		, @BzFld(property = "memEslipse")//
		, @BzFld(property = "level") //
		, @BzFld(property = "monitor")//
		, @BzFld(property = "threadname") //
		, @BzFld(property = "loginuser")//
		, @BzFld(property = "remoteUrl")//
		, @BzFld(property = "loggername") //
		, @BzFld(property = "stacktrace") //
}) }// end groups
)
public class RunningLog {
	@Id
	@Column(name = "_id")
	protected Long id;

	@Column(length = 50)
	@BzFld(name = "登录用户")
	protected String loginuser;

	protected String fqnofctgrcls;

	@BzFld(name = "日志名称")
	protected String loggername;

	@BzFld(name = "日志时间", pattern = "yyyy-MM-dd HH:mm:ss,SSS")
	protected Date datetime;

	@Column(length = 20)
	@BzFld(name = "日志级别", options = "TRACE:跟踪,DEBUG:调试,INFO:信息,WARN:警告,ERROR:错误,FATAL:致命")
	protected String level;

	@Column(length = 2000)
	@BzFld(name = "日志内容")
	protected String message;

	@BzFld(name = "线程名称")
	protected String threadname;

	@Column(columnDefinition = "text")
	@BzFld(name = "异常信息")
	protected String stacktrace;

	protected String ndc;

	@BzFld(name = "信息来源")
	protected String locationinfo;

	@BzFld(name = "远程URL")
	protected String remoteUrl;

	@BzFld(name = "远程URI")
	protected String remoteUri;

	@Column(length = 64)
	@BzFld(name = "远程IP")
	protected String remoteIp;

	@BzFld(name = "内存消耗")
	protected long memEslipse;

	@BzFld(name = "时间消耗")
	protected long eslipse;

	@BzFld(name = "资源检测")
	protected String monitor;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level.toUpperCase().trim();
	}

	public void setStackTrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public String getNdc() {
		return ndc;
	}

	public void setNdc(String ndc) {
		this.ndc = ndc;
	}

	public String getFqnofctgrcls() {
		return fqnofctgrcls;
	}

	public void setFqnofctgrcls(String fqnofctgrcls) {
		this.fqnofctgrcls = fqnofctgrcls;
	}

	public String getLoggername() {
		return loggername;
	}

	public void setLoggername(String loggername) {
		this.loggername = loggername;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setLogtime(Date logtime) {
		this.datetime = logtime;
	}

	public String getThreadname() {
		return threadname;
	}

	public void setThreadname(String threadname) {
		this.threadname = threadname;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public String getLocationinfo() {
		return locationinfo;
	}

	public void setLocationinfo(String locationinfo) {
		this.locationinfo = locationinfo;
	}

	public String getLoginuser() {
		return loginuser;
	}

	public void setLoginuser(String loginuser) {
		this.loginuser = loginuser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public String getRemoteUri() {
		return remoteUri;
	}

	public void setRemoteUri(String remoteUri) {
		this.remoteUri = remoteUri;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public long getMemEslipse() {
		return memEslipse;
	}

	public void setMemEslipse(long memEslipse) {
		this.memEslipse = memEslipse;
	}

	public long getEslipse() {
		return eslipse;
	}

	public void setEslipse(long eslipse) {
		this.eslipse = eslipse;
	}

	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
}
