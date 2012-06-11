package org.nutz.dao.impl;

import org.nutz.dao.entity.Link;

public abstract class DemsyLinkInvoker {
	public abstract void invoke(Link link, Object ta);
}
