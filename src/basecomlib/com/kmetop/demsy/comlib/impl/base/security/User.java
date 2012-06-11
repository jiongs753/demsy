package com.kmetop.demsy.comlib.impl.base.security;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_USER;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_USER;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.entity.base.BaseUser;
import com.kmetop.demsy.comlib.security.IAdminUser;

@Entity
@BzSys(name = "后台登录帐号", code = BIZSYS_ADMIN_USER, catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_USER, buildin = false//
, actions = { @BzAct(name = "添加帐号", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.security.SaveUser")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "登录帐号", property = "code", mode = "c:M *:S") //
		, @BzFld(name = "账户名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "登录密码", property = "rawPassword", password = true, gridField = false, mode = "*:N c:M e:E") //
		, @BzFld(name = "验证密码", property = "rawPassword2", password = true, gridField = false, mode = "*:N c:M e:E") //
}), @BzGrp(name = "其他信息", code = "other"//
, fields = { @BzFld(property = "group") //
		, @BzFld(property = "role") //
		, @BzFld(name = "有效期自", property = "expiredFrom", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "有效期至", property = "expiredTo", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "账户停用", property = "disabled", options = "0:启用,1:停用") //
		, @BzFld(name = "密码问题", property = "pwdQuestion", gridField = false) //
		, @BzFld(name = "密码答案", property = "pwdAnswer", gridField = false) //
		, @BzFld(name = "用户图片", property = "image", gridField = false, uploadType = "*.jpg;*.gif;*.png") //
		, @BzFld(name = "用户徽标", property = "logo", gridField = false, uploadType = "*.jpg;*.gif;*.png") //
		// , @BzFld(name = "权限有效期自", property = "permissionExpiredFrom") //
		// , @BzFld(name = "权限有效期至", property = "permissionExpiredTo") // /
		// , @BzFld(name = "最近登录地址", property = "lastedRemoteAddr", mode =
		// "*:P") //
		// , @BzFld(name = "最近登录时间", property = "lastedLoginDate", mode =
		// "v:S *:N")
		// //
		, @BzFld(name = "用户描述", property = "desc") //
		// , @BzFld(name = "登录次数", property = "loginedCount", mode = "v:S *:N")
		// //
		, @BzFld(name = "创建时间", property = "created", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "更新时间", property = "updated", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "v:S *:N") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "v:S *:N") //
}) }// end groups
, jsonData = "User.data.js"//
)
public class User extends BaseUser implements IAdminUser {

	@BzFld(name = "最近访问")
	private String latestUrl;

	@ManyToOne
	@BzFld(name = "用户分组")
	private Group group;

	@ManyToOne
	@BzFld(name = "用户角色", options = "['inner eq 0']", disabledNavi = true)
	private UserRole role;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getLatestUrl() {
		return latestUrl;
	}

	public void setLatestUrl(String latestUrl) {
		this.latestUrl = latestUrl;
	}

}
