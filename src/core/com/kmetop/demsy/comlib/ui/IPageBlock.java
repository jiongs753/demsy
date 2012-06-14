package com.kmetop.demsy.comlib.ui;

import com.kmetop.demsy.comlib.biz.field.CssPosition;
import com.kmetop.demsy.comlib.biz.field.Dataset;
import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;
import com.kmetop.demsy.comlib.entity.IBizComponent;

public interface IPageBlock extends IBizComponent {
	public static final byte TYPE_LIB = 0;// 展现构件库

	public static final byte TYPE_UDF = 1;// 自定义

	public static final byte TYPE_REF = 2;// 引用

	public static final byte TYPE_EMP = 9;// 空板块

	/**
	 * 判断是否是占位符？
	 * 
	 * @return
	 */
	boolean isPlaceHolder();

	/**
	 * 判断实际数据行数小于列表所需行数时是否填充列表空白行？
	 * 
	 * @return
	 */
	boolean isFillBlank();

	/**
	 * 板块类型
	 * 
	 * @return
	 */
	byte getType();

	/**
	 * 所属页面
	 * 
	 * @return
	 */
	IPage getPage();

	/**
	 * 上级板块
	 * 
	 * @return
	 */
	IPageBlock getParent(boolean lazy);

	/**
	 * 视图类型
	 * 
	 * @return
	 */
	IUIViewComponent getViewType();

	/**
	 * 自定义视图模版
	 * 
	 * @return
	 */
	String getViewTemplate();

	/**
	 * 自定义视图表达式
	 * 
	 * @return
	 */
	String getViewExpression();

	/**
	 * 自定义视图控制器
	 * 
	 * @return
	 */
	String getViewController();

	/**
	 * 引入页面作为该板块的视图
	 * 
	 * @return
	 */
	IPage getViewPage();

	IPage getTitleLink();

	IPage getLink();

	String getLinkTarget();

	String getTitleLinkTarget();

	CssPosition getPosition();

	void setPosition(CssPosition p);

	IStyle getStyle();

	Dataset getDataset();

	Integer getCellCount();

	Integer getTitleLength();

	boolean getHorizontal();

	String getInlineStyle();

	boolean isAllowEmptyImg();

	public String getParams();

	FakeSubSystem<? extends IStyleItem> getStyleItems();
}
