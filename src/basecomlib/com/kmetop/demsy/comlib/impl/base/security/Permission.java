package com.kmetop.demsy.comlib.impl.base.security;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_PERMISSION;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_PERMISSION;

import java.util.Date;

import javax.persistence.Entity;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSubFld;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.biz.field.Dataset;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.security.IPermission;
import com.kmetop.demsy.lang.Str;

@Entity
@BzSys(name = "功能模块授权", code = BIZSYS_ADMIN_PERMISSION, catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_PERMISSION, buildin = false//
, actions = { @BzAct(name = "新增权限", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = "com.kmetop.demsy.plugins.security.SavePermission")//
		, @BzAct(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", plugin = "com.kmetop.demsy.plugins.security.SavePermission") //
		, @BzAct(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d", plugin = "com.kmetop.demsy.plugins.security.SavePermission") //
		, @BzAct(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(property = "users") //
		, @BzFld(property = "users.module", name = "用户类型", isTransient = true, gridOrder = 1, mode = "*:N") //
		, @BzFld(property = "users.rules2", name = "用户规则", isTransient = true, gridOrder = 2, mode = "*:N") //
		, @BzFld(property = "datas") //
		, @BzFld(property = "datas.module", name = "功能模块", isTransient = true, gridOrder = 4, mode = "*:N") //
		, @BzFld(property = "datas.rules2", name = "数据权限", isTransient = true, gridOrder = 5, mode = "*:N") //
		, @BzFld(property = "actions", gridOrder = 6) //
}), @BzGrp(name = "其他属性", code = "other"//
, fields = { @BzFld(property = "expiredFrom", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(property = "expiredTo", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(property = "type", gridOrder = 3) //
		, @BzFld(name = "停用状态", property = "disabled", options = "1:停用,0:启用", disabledNavi = true) //
		, @BzFld(name = "权限描述", property = "info", isTransient = true, mode = "*:N v:S") //
		, @BzFld(name = "授权时间", property = "updated", mode = "*:P", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "授权帐号", property = "updatedBy", mode = "*:P") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:P", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
}) }// end groups
)
public class Permission extends BizComponent implements IPermission {
	@BzFld(name = "有效期自")
	protected Date expiredFrom;

	@BzFld(name = "有效期至")
	protected Date expiredTo;

	@BzFld(name = "权限类型", options = "1:允许操作,0:禁止操作", disabledNavi = true)
	protected Boolean type;

	@BzFld(name = "用户"//
	, children = {
			//
			@BzSubFld(property = "moduleGuid", name = "用户类型", options = "['refSystemExtends eq BaseUser']", order = 1)//
			, @BzSubFld(property = "rules", name = "用户名称", order = 2) //
	})
	protected Dataset users;

	@BzFld(name = "模块"//
	, children = {
			//
			@BzSubFld(property = "moduleGuid", name = "功能模块", order = 1)//
			, @BzSubFld(property = "rules", name = "数据权限", order = 2) //
	})
	protected Dataset datas;

	@BzFld(name = "模块操作")
	protected String actions;

	public String getInfo() {
		StringBuffer sb = new StringBuffer();

		if (users != null) {
			if (!Str.isEmpty(users.getModuleGuid()))
				sb.append(users.getModule());

			if (!Str.isEmpty(users.getRules2()))
				sb.append("(").append(users.getRules2()).append(")");
		}
		if (type != null && !type)
			sb.append("【禁止操作】");
		else
			sb.append("【允许操作】");
		if (datas != null) {
			if (!Str.isEmpty(datas.getModuleGuid())) {
				sb.append("功能模块(").append(datas.getModule()).append(")");
			}

			if (!Str.isEmpty(datas.getRules2()))
				sb.append("数据(").append(datas.getRules2()).append(")");
		}

		return sb.toString();
	}

	public Date getExpiredFrom() {
		return expiredFrom;
	}

	public Date getExpiredTo() {
		return expiredTo;
	}

	public boolean isDenied() {
		return type != null && type == false;
	}

	public Boolean getType() {
		return type;
	}

	public void setExpiredFrom(Date expiredFrom) {
		this.expiredFrom = expiredFrom;
	}

	public void setExpiredTo(Date expiredTo) {
		this.expiredTo = expiredTo;
	}

	public void setType(Boolean denied) {
		this.type = denied;
	}

	public Dataset getUsers() {
		return users;
	}

	public Dataset getDatas() {
		return datas;
	}

	public void setUsers(Dataset users) {
		this.users = users;
	}

	public void setDatas(Dataset datas) {
		this.datas = datas;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

}
