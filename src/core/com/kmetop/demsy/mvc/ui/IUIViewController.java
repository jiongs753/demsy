package com.kmetop.demsy.mvc.ui;

/**
 * 视图控制器：
 * 
 * @author jiongs753
 * 
 */
public interface IUIViewController {
	/**
	 * 处理业务逻辑
	 * 
	 * @param context
	 * @return 返回值可以是一个对象或Map，在模版表达式中可以通过语句访问(如：{$data})；对象将作为板块视图的数据，
	 *         Map中的所有节点都将作为模版环境数据。
	 */
	Object process(UIBlockContext context);

	/**
	 * 获取视图模版文件
	 * 
	 * @param context
	 * @param defaultView
	 * @return
	 */
	String getViewTemplate(UIBlockContext context, String defaultTemplate);

	/**
	 * 获取视图表达式，比视图模版优先级高
	 * 
	 * @param context
	 * @return
	 */
	String getViewExpression(UIBlockContext context, String defaultExpression);
}
