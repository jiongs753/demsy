package org.nutz.dao.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Link;

import com.kmetop.demsy.comlib.biz.field.SubSystem;

public final class DemsyLinks {

	public DemsyLinks(Object obj, Entity<?> en, String regex) {
		this.obj = obj;
		this.entity = en;
		ones = new LinkedList<Link>();
		manys = new LinkedList<Link>();
		mms = new LinkedList<Link>();
		all = en.getLinks(regex);
		if (null != all)
			for (Iterator<Link> it = all.iterator(); it.hasNext();) {
				Link ln = it.next();
				if (ln.isOne())
					ones.add(ln);
				else if (ln.isMany())
					manys.add(ln);
				else
					mms.add(ln);
			}

	}

	private List<Link> ones;
	private List<Link> manys;
	private List<Link> mms;
	private List<Link> all;
	private Object obj;
	private Entity<?> entity;

	boolean hasLinks() {
		return all.size() > 0;
	}

	private void invoke(DemsyLinkInvoker walker, List<Link> list) {
		if (null != list)
			for (Iterator<Link> it = list.iterator(); it.hasNext();) {
				Link link = it.next();
				Object value = entity.getMirror().getValue(obj, link.getOwnField());
				if (null != value) {
					// 保存一对多的子系统数据
					if (value instanceof SubSystem) {
						Class childType = link.getTargetClass();
						List children = ((SubSystem) value).getList(childType);
						walker.invoke(link, children);
					} else {
						// 暂不支持保存List子系统数据
						// walker.invoke(link, value);
					}
				}
			}
	}

	public void invokeOnes(DemsyLinkInvoker invoker) {
		invoke(invoker, ones);
	}

	public void invokeManys(DemsyLinkInvoker invoker) {
		invoke(invoker, manys);
	}

	public void invokeManyManys(DemsyLinkInvoker invoker) {
		invoke(invoker, mms);
	}

	public void invokeAll(DemsyLinkInvoker invoker) {
		invoke(invoker, all);
	}

	private void walk(LinkWalker walker, List<Link> list) {
		if (null != list)
			for (Iterator<Link> it = list.iterator(); it.hasNext();) {
				walker.walk(it.next());
			}
	}

	void walkOnes(LinkWalker walker) {
		walk(walker, ones);
	}

	void walkManys(LinkWalker walker) {
		walk(walker, manys);
	}

	void walkManyManys(LinkWalker walker) {
		walk(walker, mms);
	}

	void walkAll(LinkWalker walker) {
		walk(walker, all);
	}

}
