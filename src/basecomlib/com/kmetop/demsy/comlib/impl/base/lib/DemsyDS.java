package com.kmetop.demsy.comlib.impl.base.lib;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_DEMSY_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_DATASOURCE;
import static com.kmetop.demsy.comlib.LibConst.ORDER_DEMSY_DATASOURCE;

import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.kmetop.demsy.comlib.biz.ann.BzAct;
import com.kmetop.demsy.comlib.biz.ann.BzFld;
import com.kmetop.demsy.comlib.biz.ann.BzGrp;
import com.kmetop.demsy.comlib.biz.ann.BzSys;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.config.IDataSource;

@Entity
@BzSys(name = "应用系统数据源", code = BIZSYS_DEMSY_DATASOURCE, catalog = BIZCATA_DEMSY_ADMIN, orderby = ORDER_DEMSY_DATASOURCE, buildin = true//
, actions = { @BzAct(name = "新增数据源", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @BzAct(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @BzGrp(name = "基本信息", code = "basic"//
, fields = { @BzFld(name = "数据源名称", property = "name", mode = "c:M e:M")//
		, @BzFld(name = "人工顺序", property = "orderby") //
		, @BzFld(name = "数据库URL", property = "url", mode = "c:M e:M") //
		, @BzFld(name = "数据库驱动", property = "driver", mode = "c:M e:M") //
		, @BzFld(name = "数据库用户", property = "user", mode = "c:M e:M") //
		, @BzFld(name = "数据库密码", property = "pwd") //
		, @BzFld(name = "数据源描述", property = "desc") //
		, @BzFld(name = "创建时间", property = "created", mode = "*:P") //
		, @BzFld(name = "更新时间", property = "updated", mode = "*:P") //
		, @BzFld(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @BzFld(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class DemsyDS extends BizComponent implements IDataSource {
	private String url;

	private String driver;

	@Column(length = 64)
	private String user;

	@Column(length = 64)
	private String pwd;

	public String getUrl() {
		return url;
	}

	public String getDriver() {
		return driver;
	}

	public String getUser() {
		return user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Override
	public Properties getProperties() {
		return dynamicProps;
	}

}
