package com.kmetop.demsy.comlib.impl.base.lib;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Entity;

import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.entity.IDemsyCorp;
import com.kmetop.demsy.comlib.impl.BizComponent;

@Entity
@BzSys(name = "企业用户管理", code = LibConst.BIZSYS_DEMSY_CORP, catalog = LibConst.BIZCATA_DEMSY_ADMIN, orderby = LibConst.ORDER_DEMSY_CORP, buildin = true//
, actions = { @BzAct(name = "新增企业", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "企业名称", property = "name")//
		, @BzFld(name = "企业编号", property = "code")//
		, @BzFld(name = "人工顺序", property = "orderby") //
		, @BzFld(name = "软件描述", property = "desc") //
		, @BzFld(name = "内置状态", property = "buildin", mode = "*:N") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:P") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class DemsyCorp extends BizComponent implements IDemsyCorp {

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

}
