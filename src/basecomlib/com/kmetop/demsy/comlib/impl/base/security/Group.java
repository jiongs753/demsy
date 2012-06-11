package com.kmetop.demsy.comlib.impl.base.security;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_USER_GROUP;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.security.IGroup;

@Entity
@BzSys(name = "后台帐号分组", catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_USER_GROUP, buildin = false//
, actions = { @BzAct(name = "新增分组", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "分组名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "分组编号", property = "code", mode = "c:M e:M") //
		, @BzFld(property = "parent") //
		, @BzFld(name = "分组描述", property = "desc") //
		, @BzFld(name = "创建时间", property = "created", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "更新时间", property = "updated", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "v:S *:N") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "v:S *:N") //
}) }// end groups
)
public class Group extends BizComponent implements IGroup {

	@ManyToOne
	@BzFld(name = "上级分组")
	protected Group parent;

	protected boolean disabledSelected;

	public Group getParent() {
		return parent;
	}

	public void setParent(Group parent) {
		this.parent = parent;
	}

	public boolean isDisabledSelected() {
		return disabledSelected;
	}

	public void setDisabledSelected(boolean disabledSelected) {
		this.disabledSelected = disabledSelected;
	}

}
