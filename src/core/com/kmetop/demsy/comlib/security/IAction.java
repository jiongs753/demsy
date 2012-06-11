package com.kmetop.demsy.comlib.security;

import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.entity.IBizComponent;

/**
 * <b>模块操作：</b>功能模块的操作部分，最小的模块功能点。
 * <ul>
 * <li>模块操作不能单独存在，只能依附于功能模块而存在。</li>
 * </ul>
 * <p>
 * <b>操作的授权：</b>
 * <ul>
 * <li>对模块的授权分类两种：一种为模块操作的授权；另一种为模块数据的授权。</li>
 * </ul>
 * 
 * @author yongshan.ji
 */
public interface IAction extends IBizComponent {
	IAction getActionLib();

	IAction getParentAction();

	Integer getTypeCode();

	String getMode();

	String getPlugin();

	Upload getImage();

	Upload getLogo();

	/**
	 * 执行该操作时的显示的页面模版
	 * 
	 * @return
	 */
	String getTemplate();

	String getDesc();

	String getInfo();

	String getError();

	String getWarn();

	String getTargetUrl();

	String getTargetWindow();

	String getParams();
}
