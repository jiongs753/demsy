package com.kmetop.demsy.comlib.impl.sft.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.sft.SFTBizComponent;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.orm.ann.Prop;

@Entity
public class UserLogin extends SFTBizComponent implements IUser {
	private static final String PWD_ENCODER = "$1";

	private static final int MASK_DISABLED = 1;// 2^0

	private static final int MASK_LOCKED = 2;// 2^1

	private static final int MASK_BUILDIN = 4;// 2^2

	private static final int MASK_MULTIPLE_LOGIN_ENABLED = 8;

	private static final int MASK_MULTIPLE_LOGIN_ERROR = 16;

	private static final int MASK_PLAINTEXT_PASSWORD = 32;

	@Column(length = 128)
	private String password;

	@Transient
	private String rawPassword;

	@Transient
	private String rawPassword2;

	@Column(length = 255)
	private String pwdQuestion;// 忘记密码

	@Column(length = 255)
	private String pwdAnswer;

	@Prop("photo")
	private Upload image;

	private Upload logo;

	private Date expiredFrom = new Date();// 账户有效起始日期

	private Date expiredTo;// 账户有效截至日期

	@Prop("credentialsExpiredFrom")
	private Date permissionExpiredFrom = new Date();// 权限有效起始日期

	@Prop("credentialsExpiredTo")
	private Date permissionExpiredTo;// 权限有效截至日期

	private Integer loginedCount;// 登陆次数

	private String lastedRemoteAddr;// 最后登陆IP地址

	private Date lastedLoginDate;// 最后登陆时间

	@ManyToOne
	private PasswordEncodeStrategy passwordEncoder;

	@Override
	public String getUsername() {
		return getCode();
	}

	@Override
	public Long getPwdEncoder() {
		try {
			String str = get(PWD_ENCODER);
			if (str != null && str.trim().length() > 0)
				return Long.parseLong(str);
		} catch (Throwable e) {
			return null;
		}
		if (this.getPasswordEncoder() == null) {
			return null;
		}
		return this.passwordEncoder.getId();
	}

	public void setPassword(String password) {
		this.password = password;
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
		this.set(PWD_ENCODER, pwdEncoder);
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

	public boolean isPlainTextPassword() {
		return super.getMask(MASK_PLAINTEXT_PASSWORD);
	}

	public void setPlainTextPassword(boolean flag) {
		super.setMask(MASK_PLAINTEXT_PASSWORD, flag);
	}

	public boolean isMultipleLoginError() {
		return !isMultipleLoginEnable() && super.getMask(MASK_MULTIPLE_LOGIN_ERROR);
	}

	public void setMultipleLoginError(boolean flag) {
		super.setMask(MASK_MULTIPLE_LOGIN_ERROR, flag);
	}

	public boolean isMultipleLoginEnable() {
		return super.getMask(MASK_MULTIPLE_LOGIN_ENABLED);
	}

	public void setMultipleLoginEnable(boolean flag) {
		super.setMask(MASK_MULTIPLE_LOGIN_ENABLED, flag);
	}

	public boolean isDisabled() {
		return super.getMask(MASK_DISABLED);
	}

	public void setDisabled(boolean flag) {
		super.setMask(MASK_DISABLED, flag);
	}

	public boolean isLocked() {
		return super.getMask(MASK_LOCKED);
	}

	public void setLocked(boolean flag) {
		super.setMask(MASK_LOCKED, flag);
	}

	public boolean isBuildin() {
		return super.getMask(MASK_BUILDIN);
	}

	public void setBuildin(boolean flag) {
		super.setMask(MASK_BUILDIN, flag);
	}

	public String getPassword() {
		return password;
	}

	public String getPwdQuestion() {
		return pwdQuestion;
	}

	public void setPwdQuestion(String pwdQuestion) {
		this.pwdQuestion = pwdQuestion;
	}

	public String getPwdAnswer() {
		return pwdAnswer;
	}

	public void setPwdAnswer(String pwdAnswer) {
		this.pwdAnswer = pwdAnswer;
	}

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

	public Date getPermissionExpiredFrom() {
		return permissionExpiredFrom;
	}

	public void setPermissionExpiredFrom(Date credentialsExpiredFrom) {
		this.permissionExpiredFrom = credentialsExpiredFrom;
	}

	public Date getPermissionExpiredTo() {
		return permissionExpiredTo;
	}

	public void setPermissionExpiredTo(Date credentialsExpiredTo) {
		this.permissionExpiredTo = credentialsExpiredTo;
	}

	public PasswordEncodeStrategy getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncodeStrategy passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public Integer getLoginedCount() {
		return loginedCount;
	}

	public void setLoginedCount(Integer loginedCount) {
		this.loginedCount = loginedCount;
	}

	public String getLastedRemoteAddr() {
		return lastedRemoteAddr;
	}

	public void setLastedRemoteAddr(String lastedRemoteAddr) {
		this.lastedRemoteAddr = lastedRemoteAddr;
	}

	public Date getLastedLoginDate() {
		return lastedLoginDate;
	}

	public void setLastedLoginDate(Date lastedLoginDate) {
		this.lastedLoginDate = lastedLoginDate;
	}

	public Upload getImage() {
		return image;
	}

	public Upload getLogo() {
		return logo;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public void setLogo(Upload logo) {
		this.logo = logo;
	}
}
