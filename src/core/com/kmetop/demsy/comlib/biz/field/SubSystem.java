package com.kmetop.demsy.comlib.biz.field;

import java.util.ArrayList;
import java.util.List;

import com.kmetop.demsy.comlib.entity.IBizComponent;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.JSON;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.util.sort.SortUtils;

/**
 * 子系统数据：将被自动保存到子系统数据表中
 * 
 * @author yongshan.ji
 * 
 * @param <T>
 */
public class SubSystem<T> implements IExtField {
	private static Log log = Logs.get();

	private Class<T> type;
	private String jsonData;
	private List<T> list;

	public SubSystem() {
		this("");
	}

	public SubSystem(String str) {
		init(str);
	}

	public SubSystem(List<T> list) {
		if (list != null)
			SortUtils.sort(list, "orderby", true);

		this.list = list;
	}

	public SubSystem(String str, Class<T> type) {
		this.type = type;
		init(str);
	}

	private void init(String str) {
		if (Str.isEmpty(jsonData)) {
			this.jsonData = "";
		}
		this.jsonData = str;

		if (list == null && type != null) {
			list = new ArrayList();
			if (!Str.isEmpty(jsonData)) {
				try {
					list = JSON.loadFromJson(type, jsonData);
				} catch (Throwable e) {
					log.errorf("从JSON加载子系统数据出错! %s", e);
					// throw new DemsyException(e);
				}
			}
		}
	}

	public List<T> getList() {
		return list;
	}

	public List<T> getList(Class type) {
		List list = new ArrayList();
		if (!Str.isEmpty(jsonData)) {
			try {
				list = JSON.loadFromJson(type, jsonData);
			} catch (Throwable e) {
				log.errorf("从JSON加载子系统数据出错! %s", e);
				throw new DemsyException(e);
			}
		}
		if (list != null) {
			int orderby = 1;
			for (Object obj : list) {
				if (obj instanceof IBizComponent) {
					((IBizComponent) obj).setOrderby(orderby++);
				}
			}
		}

		return list;
	}

	public String toString() {
		return jsonData == null ? "" : jsonData;
	}
}
