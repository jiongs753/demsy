package com.kmetop.demsy.mvc.ui;

import java.io.Serializable;
import java.util.Map;

import com.kmetop.demsy.mvc.ui.model.UIWidgetModel;
import com.kmetop.demsy.mvc.ui.widget.UIPageView;
import com.kmetop.demsy.mvc.ui.widget.UIWidget;

/**
 * 用户界面: 用户界面分为如下几种：
 * <UL>
 * <LI>UI模型：参见 {@link UIWidget}
 * <LI>页面UI：参见 {@link UIPageView}
 * <LI>数据窗体：参见 {@link UIWidgetModel}
 * </UL>
 * 
 * @author yongshan.ji
 */
public interface IUIView {
	String getUiid();

	Serializable getId();

	String getName();

	/**
	 * 获取UI模版文件。
	 * <UL>
	 * <LI>模版文件不带任何文件后缀；
	 * <LI>格式类似于类全名，如com.kmetop.mvc.ui.Grid，默认为UI类全称；
	 * <LI>模版文件通常由两部分组成：其一为UI模型、其二为AJAX数据；
	 * <LI>UI模型文件后缀可以如下格式默认为.st：
	 * <OL>
	 * <LI>.st——Smarty模版；
	 * <LI>.ftl——Freemarker模版；
	 * <LI>.vm——Velocity模版
	 * </OL>
	 * <LI>AJAX数据文件后缀对应的可以是：
	 * <OL>
	 * <LI>.json{UI模型后缀}——Json数据，如：.json.st， .json.ftl， .json.vm
	 * <LI>.xml{UI模型后缀}——XML数据，如：.xml.st， .xml.ftl， .xml.vm
	 * <LI>.html{UI模型后缀}——HTML数据，如：.html.st， .html.ftl， .html.vm
	 * </OL>
	 * <LI>
	 * 如：UI模型文件——com/kmetop/mvc/ui/BizGrid.st、数据文件——com/kmetop/mvc/ui/BizGrid
	 * .st.xml
	 * </UL>
	 * 
	 * @return
	 */
	String getTemplate();

	/**
	 * 类似于template模版文件中的内容
	 * 
	 * @return
	 */
	String getExpression();

	/**
	 * 获取模版类型：为了方便模版类型以 . 开头
	 * <p>
	 * UI模型文件后缀可以如下格式默认为.st：
	 * <OL>
	 * <LI>.st——Smarty模版；
	 * <LI>.ftl——Freemarker模版；
	 * <LI>.vm——Velocity模版
	 * </OL>
	 * 
	 * @return
	 */
	String getTemplateType();

	/**
	 * 获取通过AJAX方式加载的数据类型：数据类型不以 . 开头
	 * 
	 * <LI>AJAX数据文件后缀对应的可以是：
	 * <OL>
	 * <LI>json——Json数据，如：.json.st， .json.ftl， .json.vm
	 * <LI>xml——XML数据，如：.xml.st， .xml.ftl， .xml.vm
	 * <LI>html——HTML数据，如：.html.st， .html.ftl， .html.vm
	 * </OL>
	 * 
	 * @return
	 */
	String getDataType();

	/**
	 * 模板环境变量
	 * 
	 * @return
	 */
	Map getContext();

}
