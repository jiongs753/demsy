package com.kmetop.demsy.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.config.IAppConfig;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Ex;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public abstract class Plugins {
	public static final Log log = Logs.getLog(Plugins.class);

	private static Map<Class, Object> pluginMap = new HashMap();

	// <环境插件配置KEY, 插件对象>
	private static Map<String, IContextPlugin> contextPluginMap = new HashMap();

	public static void startContextPlugins() {
		log.infof("启动 Plugin(s)......");

		// TODO: 从配置文件中加载环境插件：通过环境插件前缀查找
		List contextPlugins = new LinkedList();
		contextPlugins.add("com.kmetop.demsy.plugin.impl.QuartzContextPlugin");

		for (Object className : contextPlugins) {
			IContextPlugin plugin = null;
			try {
				if (className instanceof String) {
					Class cls = Cls.forName((String) className);
					plugin = (IContextPlugin) Mirror.me(cls).born();
				} else if (className instanceof IContextPlugin) {
					plugin = (IContextPlugin) className;
				}

				if (plugin.support()) {
					plugin.start();

					log.infof("启动 Plugin: 结束. [%s, class=%s]", plugin.getName(), className);
				}
			} catch (Throwable e) {
				log.errorf("启动 Plugin 出错! [%s, class=%s] %s", plugin == null ? "NULL" : plugin.getName(), className, Ex.msg(e));
			}
		}
		log.infof("启动 Plugin(s): 结束. [size=%s]", contextPlugins.size());
	}

	public static void closeContextPlugins() throws DemsyException {
		log.infof("停止 Plugin(s)...... [size=%s]", contextPluginMap.size());

		Collection<IContextPlugin> contextPlugins = contextPluginMap.values();
		for (IContextPlugin plugin : contextPlugins) {
			try {
				if (plugin.support()) {
					plugin.close();

					log.infof("停止  Plugin: 结束. [%s, class=%s]", plugin.getName(), plugin.getClass().getName());
				}
			} catch (Throwable e) {
				log.errorf("停止 Plugin 出错! [%s, class=%s] %s", plugin.getName(), plugin.getClass().getName(), Ex.msg(e));
			}
		}

		log.infof("停止 Plugin(s): 结束. [size=%s]", contextPlugins.size());
	}

	public static IBbsPlugin getBbsPlugin() {
		return getPlugin(IBbsPlugin.class);
	}

	public static <T> T getPlugin(Class<T> interfaceOfPlugin) {
		synchronized (pluginMap) {
			Object plugin = pluginMap.get(interfaceOfPlugin);
			if (plugin == null) {

				String key = IAppConfig.PLUGIN_PREFIX + interfaceOfPlugin.getSimpleName();
				try {
					String pluginImpl = Demsy.appconfig.get(key);
					plugin = Class.forName(pluginImpl).newInstance();
					pluginMap.put(interfaceOfPlugin, plugin);

					log.infof("加载 Plugin: 结束. [key=%s, plugin=%s]", key, plugin.getClass().getName());
				} catch (Throwable e) {
					log.errorf("加载 Plugin 出错! [key=%s] %s", key, e);
				}

			}
			return (T) plugin;
		}
	}
}
