package com.kmetop.demsy.comlib.impl.base.security;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_USER_ROLE;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.security.IUserRole;

@Entity
@BzSys(name = "后台帐号角色", catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_USER_ROLE, buildin = false//
, actions = { @BzAct(name = "新增分组", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "角色名称", property = "name", mode = "c:M e:M")//
		, @BzFld(property = "type") //
		, @BzFld(property = "parent") //
		, @BzFld(name = "角色描述", property = "desc") //
		, @BzFld(name = "创建时间", property = "created", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "更新时间", property = "updated", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "v:S *:N") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "v:S *:N") //
}) }// end groups
, jsonData = "UserRole.data.js"//
)
public class UserRole extends BizComponent implements IUserRole {

	@ManyToOne
	@BzFld(name = "上级角色")
	protected UserRole parent;

	protected boolean inner;

	@BzFld(name = "角色类型", mode = "c:M e:M", disabledNavi = true, options = "90:普通管理员,100:超级管理员")
	protected byte type;

	@OneToMany(mappedBy = "parent")
	protected List<UserRole> children;

	public UserRole getParent() {
		return parent;
	}

	public void setParent(UserRole parent) {
		this.parent = parent;
	}

	public boolean isInner() {
		return inner;
	}

	public void setDisabledSelected(boolean disabledSelected) {
		this.inner = disabledSelected;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte level) {
		this.type = level;
	}

}
