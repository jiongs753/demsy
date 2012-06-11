package com.kmetop.demsy.comlib.security;

import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.IBizComponent;
import com.kmetop.demsy.comlib.entity.ITreeEntity;
import com.kmetop.demsy.config.IDataSource;

/**
 * <b>模块：</b> 也称为功能模块，即系统提供给用户使用的功能模块。
 * <p>
 * <b>模块授权：</b>
 * <p>
 * 可以将模块授权给“功能角色”；可以将模块授权给“用户”；可以将模块授权给“组”。
 * <p>
 * <b>模块分类：</b>
 * <p>
 * 分类模块： 不包含具体的功能操作，通常没有链接地址，不能链接到特定的模块页面，用来对模块进行归类。
 * <p>
 * 业务模块： 用于完成基本的业务数据处理。
 * <p>
 * 报表模块： 用于完成数据分析统计。
 * <p>
 * 流程模块： 用于调度多任务按特定流程执行。
 * <p>
 * 静态模块： 以上模块都是动态模块，静态模块即具有独立的MVC部分，即具有独立的Action类、数据实体、展现JSP等。
 * <p>
 * <b>备注：</b>
 * <p>
 * 模块具有树形递归结构，通过分类模块对模块进行归类后从而形成了模块的树形递归结构。
 * 
 * @author yongshan.ji
 */
public interface IModule<T> extends IBizComponent, ITreeEntity<T> {
	public static final int TYPE_FOLDER = 90;

	public static final int TYPE_STATIC = 1;

	public static final int TYPE_BIZ = 2;

	public static final int TYPE_WEB = 3;

	public static final int TYPE_RPT = 4;

	public static final int TYPE_WFL = 5;

	public static final int TYPE_DYN = 6;

	IDataSource getDataSource();

	/**
	 * 获取模块徽标
	 * 
	 * @return
	 */
	Upload getLogo();

	/**
	 * 获取模块图片
	 * 
	 * @return
	 */
	Upload getImage();

	/**
	 * 获取静态模块的模块访问路径，动态模块的模块访问路径是动态生成的，因此不存在固定的模块访问路径。
	 * 
	 * @return
	 */
	String getPath();

	/**
	 * 获取模块绑定的组件ID
	 * <p>
	 * 可以绑定到模块使用的组件包括：业务系统/报表/流程/网站栏目等。
	 * <UL>
	 * <li>业务模块：绑定业务系统ID
	 * <li>报表模块：绑定报表ID
	 * <li>流程模块：绑定流程ID
	 * <li>网站栏目：绑定网站栏目ID
	 * </UL>
	 * 
	 * @return 组件ID
	 */
	Long getRefID();

	/**
	 * 获取模块绑定的操作ID列表，ID之间用竖线‘|’分隔。
	 * <UL>
	 * <LI>不同类型的模块级组件（如：业务系统/报表/流程/网站栏目等）可以拥有自己的组件操作，如：业务系统拥有业务操作。
	 * <LI>组件被绑定到模块后，可以同时将组件操作的全部或部分绑定到模块上。
	 * <LI>如果未绑定任何组件操作，则默认自动绑定组件的所有操作。
	 * <LI>绑定的操作必须是组件的操作，不能是绑定组件以外的操作。
	 * </UL>
	 * 
	 * @return
	 */
	String getRefActions();

	public int getType();

	Long getSoftID();

	boolean isHidden();
}
