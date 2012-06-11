package com.kmetop.demsy.plugin;

/**
 * 系统启动成功后或结束前调用的插件
 * 
 * @author yongshan.ji
 */
public interface IContextPlugin {
	public boolean support();

	public void start() throws Exception;

	public void close() throws Exception;

	public String getName();
}
