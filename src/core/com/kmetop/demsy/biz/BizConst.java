package com.kmetop.demsy.biz;

public interface BizConst {
	/*
	 * 业务窗体主界面组成部分
	 */
	public static final int TYPE_BZMAIN = 1;

	public static final int TYPE_BZSYS = 2;

	public static final int TYPE_BZGRID = 3;

	public static final int TYPE_BZMENU = 4;

	public static final int TYPE_BZNAVI = 5;

	public static final int TYPE_BZSYS_COMB_EXPR = 21;

	public static final int TYPE_BZSYS_COMB_FK = 22;

	public static final int TYPE_BZSYS_COMB_CATALOG_EXPR = 23;

	/*
	 * 打开业务表单，准备执行业务逻辑
	 */
	public static final int TYPE_BZFORM_NEW = 101;

	public static final int TYPE_BZFORM_EDIT = 102;

	public static final int TYPE_BZFORM_EDIT_N = 103;

	public static final int TYPE_BZFORM_EXEC_SYNC = 104;

	public static final int TYPE_BZFORM_EXEC_ASYN = 105;

	public static final int TYPE_BZFORM_PRINT = 106;

	/**
	 * 业务表单：导出业务数据到EXCEL文件
	 */
	public static final int TYPE_BZFORM_EXPORT_XLS = 107;

	/**
	 * 业务表单：从EXCEL导入业务数据到业务系统
	 */
	public static final int TYPE_BZFORM_IMPORT_XLS = 108;

	public static final int TYPE_BZFORM_ADD_N = 109;

	public static final int TYPE_BZFORM_LOAD = 199;

	/*
	 * 从业务表单中执行业务逻辑
	 */
	public static final int TYPE_BZ_SAVE = 201;

	public static final int TYPE_BZ_EXEC_SYNC = 204;

	public static final int TYPE_BZ_EXEC_ASYN = 205;

	/**
	 * 导出业务数据到EXCEL文件
	 */
	public static final int TYPE_BZ_EXPORT_XLS = 207;

	/**
	 * 从EXCEL导入业务数据到业务系统
	 */
	public static final int TYPE_BZ_IMPORT_XLS = 208;

	public static final int TYPE_BZ_ORDERBY = 250;

	public static final int TYPE_BZ_ORDERBY_UP = 251;

	public static final int TYPE_BZ_ORDERBY_DOWN = 252;

	public static final int TYPE_BZ_ORDERBY_REVERSE = 253;

	public static final int TYPE_BZ_ORDERBY_TOP = 254;

	public static final int TYPE_BZ_ORDERBY_BOTTOM = 255;

	public static final int TYPE_BZ_ORDERBY_CANCEL = 256;

	public static final int TYPE_BZ_DEL = 299;

	public static final int TYPE_BZ_CLEAR = 298;

	/*
	 * 901: 自动产生修改菜单组
	 */
	public static final int TYPE_BZ_AUTO_MAKED_UPDATE_MENUS = 901;
}
