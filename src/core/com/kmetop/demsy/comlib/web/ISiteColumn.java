package com.kmetop.demsy.comlib.web;

import java.util.List;

/**
 * 网站栏目
 * 
 * @author yongshan.ji
 * 
 */
public interface ISiteColumn {
	/**
	 * 获取上级栏目
	 * 
	 * @return
	 */
	ISiteColumn getParent();

	/**
	 * 获取下级栏目
	 * 
	 * @return
	 */
	List<? extends ISiteColumn> getChildren();

	/**
	 * 判断是否禁用栏目：被“禁用”的栏目不能被模版使用
	 * 
	 * @return
	 */
	boolean isDisabled();

	/**
	 * 判断是否禁用功能模块： 被“禁用功能模块”的栏目不能加载到功能模块菜单中。
	 * 
	 * @return
	 */
	boolean isDisabledModule();

	/**
	 * 判断是否支持栏目自定义：用户只能在“支持自定义”的栏目下添加子栏目。
	 * 
	 * @return
	 */
	boolean isEnabledCustom();

	/**
	 * 判断是否隐藏栏目：“隐藏栏目”不出现在前台栏目菜单和栏目内容导航中
	 * 
	 * @return
	 */
	boolean isHidden();

	/**
	 * 获取栏目链接路径：如果没有指定栏目链接路径，则将自动生成导航栏目信息的URL地址。
	 * 
	 * @return
	 */
	String getLinkPath();

	/**
	 * 获取栏目链接目标：默认为_self
	 * 
	 * @return
	 */
	String getLinkTarget();

	/**
	 * 获取栏目搜索关键字：便于搜索引擎搜索网页
	 * 
	 * @return
	 */
	String getKeywords();

	/**
	 * 获取栏目徽标
	 * 
	 * @return
	 */
	String getLogo();

	/**
	 * 获取栏目图片
	 * 
	 * @return
	 */
	String getImage();

	/**
	 * 判断是否禁用信息发布: 不能在被“禁用信息发布”的栏目下发布信息，即被“禁用信息发布”的栏目相当于一个归类栏目。
	 * 
	 * @return
	 */
	boolean isInfoDisabled();

	/**
	 * 判断栏目信息是否支持“随文投票调查”：如果支持，则发布信息时将允许同时发布随文调查。
	 * 
	 * @return
	 */
	boolean isInfoEnabledVoting();

	/**
	 * 判断信息摘要是否必填：在“信息摘要必需”的栏目下发布信息时，信息摘要不能为空。
	 * 
	 * @return
	 */
	boolean isInfoRequiredSumm();

	/**
	 * 判断信息图片是否必需：在“信息图片必需”的栏目下发布信息时，必须上传或提供图片路径。
	 * 
	 * @return
	 */
	boolean isInfoRequiredImage();

	/**
	 * 判断是否支持动态加载信息：从“支持动态加载信息”的栏目加载信息时，如果该栏目下没有可用信息，则自动从上级栏目加载。
	 * 
	 * @return
	 */
	boolean isInfoEnabledDynaLoad();

	/**
	 * 判断是否禁用信息搜索
	 * 
	 * @return
	 */
	boolean isInfoDisabledSearch();

	/**
	 * 获取信息发布类型：可以支持多类型信息发布，多种类型用‘|’分隔。
	 * <p>
	 * 默认支持所有类型的信息发布。
	 * <p>
	 * 0-网页设计；2-链接。
	 * 
	 * @return
	 */
	String getInfoPublishType();

	/**
	 * 获取信息发布策略：0-默认、1-即时发布、2-按流程发布
	 * 
	 * @return
	 */
	Byte getInfoPublishPolicy();

	/**
	 * 获取评论许可类型：0-默认；1-禁止评论；2-匿名评论；3-实名评论
	 * 
	 * @return
	 */
	Byte getCommentPermissionType();

	/**
	 * 获取评论发布策略：0-默认；1-禁止发布；2-即时发布；3-检查关键字后发布；4-人工发布
	 * 
	 * @return
	 */
	Byte getCommentPublishPolicy();
}
