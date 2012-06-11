package com.kmetop.demsy.comlib.security;

import java.util.Date;

import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.IBizComponent;

/**
 * <b> 用户: </b> 使用DEMSY系统功能的人。
 * <p>
 * <b>用户可以有一下几种方式使用DEMSY提供的功能</b>
 * <p>
 * 1. 任何人均可使用授权给特殊功能角色（任何人）的功能模块
 * <p>
 * 2. 拥有特殊功能角色（超级用户）的人可以使用DEMSY提供的所有功能模块
 * <p>
 * 3. 上述两种以外的人可以根据其拥有的功能角色来使用对应的功能模块
 * 
 * @author yongshan.ji
 */
public interface IUser extends IBizComponent {

	String getUsername();

	/**
	 * 获取加密后的密码
	 * 
	 * @return
	 */
	String getPassword();

	Upload getImage();

	Upload getLogo();

	Long getPwdEncoder();

	void setUsername(String username);

	/**
	 * 设置原始密码：即尚未加密的密码
	 * 
	 * @param rawPwd
	 */
	void setRawPassword(String rawPwd);

	void setRawPassword2(String rawPassword2);

	void setPwdEncoder(Long pwdEncoder);

	public Date getExpiredFrom();

	public Date getExpiredTo();

	public boolean isLocked();

}
