package com.kmetop.demsy.comlib;

public interface LibConst {
	/*
	 * 实体属性(字段)名称
	 */
	static final String F_SOFT_ID = "softID";

	static final String F_TYPE = "type";

	static final String F_VERSION = "version";

	static final String F_ID = "id";

	static final String F_GUID = "entityGuid";

	static final String F_TIME_ID = "timeID";

	static final String F_CREATED = "created";

	static final String F_CREATED_BY = "createdBy";

	static final String F_CREATED_IP = "createdIP";

	static final String F_UPDATED = "updated";

	static final String F_UPDATED_BY = "updatedBy";

	static final String F_UPDATED_IP = "updatedIP";

	static final String F_PARENT = "parent";

	static final String F_CATALOG = "catalog";

	static final String F_REFRENCE_SYSTEM = "refrenceSystem";

	static final String F_ORDER_BY = "orderby";

	static final String F_USAGE = "usage";

	static final String F_SYSTEM = "system";

	static final String F_UI_PAGE = "page";

	static final String F_GROUP_BY = "groupBy";

	static final String F_GRID_ORDER = "gridOrder";

	static final String F_DATA_GROUP = "dataGroup";

	static final String F_BUILDIN = "buildin";

	static final String F_DISABLED = "disabled";

	static final String F_NAME = "name";

	static final String F_CODE = "code";

	static final String F_CORP = "corp";

	static final String F_REF_SYSTEM = "refSystem";

	static final String F_MODE = "mode";

	static final String F_HIDDEN = "hidden";

	static final String F_TYPE_CODE = "typeCode";

	static final String F_PROP_NAME = "propName";

	static final String F_UPGRADE_FROM = "upgradeFrom";

	static final String F_RESOURCE = "resource";

	static final String F_DOMAIN = "domain";

	static final String F_PARENT_RESOURCE = "parentResource";

	// 业务分类——编号
	static final String BIZCATA_DEMSY_ADMIN = "_demsy_admin";// 平台管理

	static final String BIZCATA_UDF_CONSOLE = "_demsy_console";// 自定义管理

	static final String BIZCATA_ADMIN = "_soft_admin";//

	static final String BIZCATA_BASE = "_soft_base";// 基础数据维护

	static final String BIZCATA_WEB = "_soft_web";// 网站信息维护

	// 平台管理
	static final String BIZSYS_DEMSY_CORP = "_demsy_corp";

	static final String BIZSYS_DEMSY_SOFT = "_demsy_soft";

	static final String BIZSYS_DEMSY_DATASOURCE = "_demsy_datasource";

	static final String BIZSYS_DEMSY_LIB_ACTION = "_demsy_lib_action";

	static final String BIZSYS_DEMSY_LIB_FIELD = "_demsy_lib_field";

	static final String BIZSYS_DEMSY_LIB_ENCODER = "_demsy_lib_encoder";

	static final String BIZSYS_DEMSY_LIB_UIMODEL = "_demsy_lib_uimodel";

	// 自定义
	static final String BIZSYS_BZUDF_CATALOG = "_biz_catalog";

	static final String BIZSYS_BZUDF_SYSTEM = "_biz_system";

	static final String BIZSYS_BZUDF_FIELD_GROUP = "_biz_field_group";

	static final String BIZSYS_BZUDF_FIELD = "_biz_field";

	static final String BIZSYS_BZUDF_ACTION = "_biz_action";

	static final String BIZSYS_UIUDF_STYLE = "_ui_style";

	static final String BIZSYS_UIUDF_PAGE = "_ui_page";

	static final String BIZSYS_UIUDF_PAGE_BLOCK = "_ui_page_block";

	/*
	 * 业务系统编号：网站管理部分
	 */
	static final String BIZSYS_WEB_ADVERT = "_ui_advert";

	static final String BIZSYS_WEB_VOTE = "web_vote";

	// 系统管理
	static final String BIZSYS_ADMIN_CONFIG = "_soft_config";

	static final String BIZSYS_ADMIN_REALM = "_soft_realm";//

	static final String BIZSYS_ADMIN_MODULE = "_soft_module";

	static final String BIZSYS_ADMIN_PERMISSION = "_soft_permission";

	static final String BIZSYS_ADMIN_USER = "_soft_administrator";

	static final String BIZSYS_ADMIN_WEBUSER = "_WebUser";

	static final String BIZSYS_ADMIN_UPLOAD = "_soft_upload";

	// 平台管理
	static final int ORDER_DEMSY_LIB_FIELD = -100 + 1;

	static final int ORDER_DEMSY_LIB_ACTION = -100 + 2;

	static final int ORDER_DEMSY_LIB_ENCODER = -100 + 3;

	static final int ORDER_DEMSY_LIB_UI_MODEL = -100 + 4;

	static final int ORDER_DEMSY_DATASOURCE = -100 + 5;

	static final int ORDER_DEMSY_CORP = -100 + 6;

	static final int ORDER_DEMSY_SOFT = -100 + 7;

	static final int ORDER_DEMSY_LOG = -100 + 99;

	// 自定义
	// 100+ 业务自定义
	static final int ORDER_BZUDF_CATALOG = -100 + 10;

	static final int ORDER_BZUDF_SYSTEM = -100 + 11;

	static final int ORDER_BZUDF_FIELD_GROUP = -100 + 12;

	static final int ORDER_BZUDF_FIELD = -100 + 13;

	static final int ORDER_BZUDF_ACTION = -100 + 14;

	// 120+ 界面自定义
	static final int ORDER_UIUDF_CATALOG = -100 + 20;

	static final int ORDER_UIUDF_CATALOG_THEME = -100 + 21;

	static final int ORDER_UIUDF_CATALOG_STYLE = -100 + 22;

	static final int ORDER_UIUDF_THEME_STYLE = -100 + 23;

	static final int ORDER_UIUDF_TEMPLATE = -100 + 24;

	static final int ORDER_UIUDF_TEMPLATE_BLOCK = -100 + 25;

	// 系统管理
	// 200+
	static final int ORDER_SYSADMIN_CONFIG = -100 + 30;

	static final int ORDER_SYSADMIN_REALM = -100 + 31;

	static final int ORDER_SYSADMIN_USER_ROLE = -100 + 32;

	static final int ORDER_SYSADMIN_USER_GROUP = -100 + 33;

	static final int ORDER_SYSADMIN_USER = -100 + 34;

	static final int ORDER_SYSADMIN_WEBUSER = -100 + 35;

	static final int ORDER_SYSADMIN_MODULE = -100 + 36;

	static final int ORDER_SYSADMIN_PERMISSION = -100 + 37;

	static final int ORDER_SYSADMIN_UPLOAD = -100 + 38;

	// 网站管理
	static final int ORDER_WEB_INFO_CATALOG = 1;

	static final int ORDER_WEB_INFO = 2;

	static final int ORDER_WEB_COMMENT = 3;

	static final int ORDER_WEB_VOTE = 4;

	static final int ORDER_WEB_RESEARCH_CATALOG = 5;

	static final int ORDER_WEB_RESEARCH = 6;

	static final int ORDER_WEB_ADVERT = 7;

	static final int ORDER_WEB_FORUM_CATALOG = 8;

	static final int ORDER_WEB_FORUM_TOPIC = 9;

	static final int ORDER_WEB_FORUM_REPLY = 10;

	static final int ORDER_WEB_BLOG_TOPIC = 11;

	static final int ORDER_WEB_BLOG_COMMENT = 12;

	/*
	 * module
	 */
	static final String MODULE_OTHER = "_module_other";

	public static final int DEFAULT_PRECISION = 255;

	public static final int INPUT_FIELD_MAX_LENGTH = 255;
}
