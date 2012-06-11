package com.kmetop.demsy.comlib.impl.base.lib;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_DEMSY_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_CORP;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_DATASOURCE;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_SOFT;
import static com.kmetop.demsy.comlib.LibConst.ORDER_DEMSY_SOFT;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.lang.Str;

@Entity
@BzSys(name = "企业软件管理", code = BIZSYS_DEMSY_SOFT, catalog = BIZCATA_DEMSY_ADMIN, orderby = ORDER_DEMSY_SOFT, buildin = true//
, actions = { @BzAct(name = "新增应用", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "软件名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "软件用户", property = "corp", groupBy = true, refrenceSystem = BIZSYS_DEMSY_CORP, mode = "c:M e:M") //
		, @BzFld(name = "软件域名", property = "domain") //
		, @BzFld(name = "登录帐号", property = "code", mode = "c:M e:M *:S") //
		, @BzFld(name = "登录密码", property = "rawPassword", password = true, gridField = false, mode = "*:N c:E e:E") //
		, @BzFld(name = "验证密码", property = "rawPassword2", password = true, gridField = false, mode = "*:N c:E e:E") //
		, @BzFld(name = "密码问题", property = "pwdQuestion", gridField = false) //
		, @BzFld(name = "密码答案", property = "pwdAnswer", gridField = false) //
		, @BzFld(name = "有效期自", property = "expiredFrom") //
		, @BzFld(name = "有效期至", property = "expiredTo") //
		, @BzFld(name = "软件徽标", property = "logo", uploadType = "*.jpg;*.gif;*.png") //
		, @BzFld(name = "软件图片", property = "image", uploadType = "*.jpg;*.gif;*.png") //
		, @BzFld(name = "软件数据源", property = "dataSource", refrenceSystem = BIZSYS_DEMSY_DATASOURCE) //
		, @BzFld(name = "软件描述", property = "desc") //
		, @BzFld(name = "停用状态", property = "disabled", options = "1:停用,0:启用") //
		, @BzFld(name = "锁定状态", property = "locked", options = "1:锁定,0:未锁定") //
		, @BzFld(name = "人工顺序", property = "orderby") //
		, @BzFld(name = "内置状态", property = "buildin", mode = "*:N") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:P") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class DemsySoft extends BizComponent implements IDemsySoft {

	@ManyToOne
	private DemsyCorp corp;// 软件所属的企业

	@ManyToOne
	private DemsyDS dataSource;

	@Column(length = 64)
	private String domain;

	@Column(length = 128)
	protected String password;

	@Transient
	protected String rawPassword;

	@Transient
	protected String rawPassword2;

	@Column(length = 255)
	protected String pwdQuestion;// 忘记密码

	@Column(length = 255)
	protected String pwdAnswer;

	protected Upload image;//

	protected Upload logo;//

	protected Long pwdEncoder;

	protected Boolean locked;

	protected Date expiredFrom = new Date();

	protected Date expiredTo;

	public Date getExpiredFrom() {
		return expiredFrom;
	}

	public void setExpiredFrom(Date expiredFrom) {
		this.expiredFrom = expiredFrom;
	}

	public Date getExpiredTo() {
		return expiredTo;
	}

	public void setExpiredTo(Date expiredTo) {
		this.expiredTo = expiredTo;
	}

	public boolean isLocked() {
		return locked != null && locked;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return getCode();
	}

	public Long getPwdEncoder() {
		return pwdEncoder;
	}

	public void setUsername(String code) {
		setCode(code);
	}

	public void setCode(String code) {
		super.setCode(code);
		this.encodePwd();
	}

	public void setRawPassword(String pwd) {
		this.rawPassword = pwd;
		this.encodePwd();
	}

	public void setRawPassword2(String rawPassword2) {
		this.rawPassword2 = rawPassword2;
		this.encodePwd();
	}

	public void setPwdEncoder(Long pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
		this.encodePwd();
	}

	private void encodePwd() {
		if (!Str.isEmpty(code) && !Str.isEmpty(rawPassword) && !Str.isEmpty(rawPassword2)) {
			if (!rawPassword.equals(rawPassword2)) {
				throw new SecurityException("两次密码不一致!");
			}
			this.password = Demsy.security.encrypt(code, rawPassword, getPwdEncoder());
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRawPassword() {
		return rawPassword;
	}

	public String getRawPassword2() {
		return rawPassword2;
	}

	public String getPwdQuestion() {
		return pwdQuestion;
	}

	public String getPwdAnswer() {
		return pwdAnswer;
	}

	public Upload getImage() {
		return image;
	}

	public Upload getLogo() {
		return logo;
	}

	public void setPwdQuestion(String pwdQuestion) {
		this.pwdQuestion = pwdQuestion;
	}

	public void setPwdAnswer(String pwdAnswer) {
		this.pwdAnswer = pwdAnswer;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public void setLogo(Upload logo) {
		this.logo = logo;
	}

	public DemsyCorp getCorp() {
		return corp;
	}

	public void setCorp(DemsyCorp enterprise) {
		this.corp = enterprise;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @deprecated
	 */
	public void setSoftID(Long id) {
		// throw new java.lang.UnsupportedOperationException("不能为实体设置应用ID");
	}

	/**
	 * @deprecated
	 */
	public Long getSoftID() {
		// throw new java.lang.UnsupportedOperationException("不能获取实体应用ID");
		return null;
	}

	public DemsyDS getDataSource() {
		return dataSource;
	}

	public void setDataSource(DemsyDS dataSource) {
		this.dataSource = dataSource;
	}
}
