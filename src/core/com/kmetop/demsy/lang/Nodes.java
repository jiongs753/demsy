package com.kmetop.demsy.lang;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kmetop.demsy.comlib.entity.IDynamic;
import com.kmetop.demsy.util.sort.SortUtils;

public final class Nodes implements IDynamic {
	private Map<String, Node> nodeMap;

	private List<Node> children;

	protected Integer maskValue;

	protected Properties dynamicProps = new Properties();

	public void optimizeRoot() {
		List<Node> list = getChildren();
		if (list.size() == 1) {
			Node node = list.get(0);
			List<Node> children = node.getChildren();
			this.children.remove(0);
			for (Node child : children) {
				this.children.add(child);
				child.setParent(null);
			}
		}
		for (Node child : children) {
			child.setStatus("open");
		}
	}

	public void optimize() {
		List<Node> list = getChildren();
		for (int i = list.size() - 1; i >= 0; i--) {
			removeEmptyFolder(list, i);
		}

		for (int i = list.size() - 1; i >= 0; i--) {
			Node node = list.get(i);
			List<Node> children = node.getChildren();
			for (int j = children.size() - 1; j >= 0; j--) {
				Node child = children.get(j);
				move1ChildFolder(child);
			}
		}
	}

	// 处理只有一个儿子的文件夹
	private void move1ChildFolder(Node node) {
		if ("folder".equals(node.getType())) {
			if (node.getSize() == 1) {
				Node child = node.getChildren().get(0);

				Node parent = node.getParent();
				if (parent != null) {
					// 将单个儿子节点往上移动
					parent.getChildren().add(child);
					child.setParent(parent);

					// 并从当前节点中移除
					node.getChildren().remove(0);
					parent.getChildren().remove(node);

					// 继续网上移动单个儿子节点
					move1ChildFolder(child);
				}
			}
		}
	}

	// 移除空文件夹
	private void removeEmptyFolder(List<Node> children, int index) {
		Node node = children.get(index);
		if (node.getParams() == null) {
			if (node.getSize() != 0) {
				List<Node> list2 = node.getChildren();
				for (int i2 = list2.size() - 1; i2 >= 0; i2--) {
					removeEmptyFolder(list2, i2);
				}
			}
			if (node.getSize() == 0) {
				children.remove(index);
			}
		}
	}

	public static Nodes make() {
		return new Nodes();
	}

	private Nodes() {
		nodeMap = new HashMap();
		children = new ArrayList();
	}

	public int getTotal() {
		int total = this.getSize();
		for (Node node : children) {
			total += node.getTotal();
		}

		return total;
	}

	public boolean existNode(Serializable nodeID) {
		return nodeMap.get(Obj.toKey(nodeID)) != null;
	}

	private Node getNode(Serializable nodeID) {
		String key = Obj.toKey(nodeID);
		if (key.length() == 0) {
			return null;
		}

		Node node = nodeMap.get(key);

		if (node == null) {
			node = new Node(key);
			nodeMap.put(key, node);
		}

		return node;
	}

	public Node addNode(Serializable parentID, Serializable nodeID) {
		return this.addNode(parentID, nodeID, -1);
	}

	public Node addNode(Serializable parentID, Serializable nodeID, int index) {
		Node p = getNode(parentID);
		Node n = getNode(nodeID);

		if (n != null) {
			if (p != null) {
				if (!p.getChildren().contains(n))
					p.addChild(n);
			} else if (!children.contains(n)) {
				if (index >= 0)
					children.add(index, n);
				else
					children.add(n);
			}
		}

		return n;
	}

	public Nodes addChild(Node child) {
		child.parent = null;
		children.add(child);

		return this;
	}

	public int getSize() {
		if (children == null) {
			return 0;
		}
		return children.size();
	}

	public List<Node> getChildren() {
		return children;
	}

	public Iterator<Node> getAll() {
		return this.nodeMap.values().iterator();
	}

	@Override
	public Properties getDynaProp() {
		return dynamicProps;
	}

	public void setDynaProp(Properties props) {
		dynamicProps = props;
	}

	@Override
	public String get(String key) {
		return (String) dynamicProps.get(key);
	}

	@Override
	public void set(String key, Object value) {
		if (value == null) {
			dynamicProps.remove(key);
		} else if (value instanceof String) {
			dynamicProps.put(key, value);
		} else {
			dynamicProps.put(key, value.toString());
		}
	}

	public void set(byte index, boolean flag) {
		int MASK = new Double(Math.pow(2, new Double(index - 1))).intValue();
		if (maskValue == null) {
			maskValue = 0;
		}
		synchronized (maskValue) {
			if (flag) {// 隐藏
				maskValue |= MASK;
			} else {// 不隐藏
				int mask = ~MASK;// 反码
				maskValue &= mask;// 与
			}
		}
	}

	@Override
	public boolean is(byte index) {
		int MASK = new Double(Math.pow(2, new Double(index - 1))).intValue();
		if (maskValue == null) {
			return false;
		}
		synchronized (maskValue) {
			return (maskValue & MASK) > 0;
		}
	}

	public static final class Node implements IDynamic {
		private List<Node> children = new ArrayList();

		private Node parent;

		private Serializable type = "leaf";// 节点类型

		private Serializable id;// 节点ID

		private String name;// 节点名称

		private String desc = "";// 节点描述

		private String icon;// 节点图标

		private String status;// 节点状态：open | close

		private Integer order;

		// private String action;// 点击节点是的动作

		private Object params;// 处理节点动作时可能用到的参数值

		protected Integer maskValue;

		protected Map dynamicProps = new HashMap();

		private Node(Serializable id) {
			this.id = id;
		}

		public int getTotal() {
			int total = this.getSize();
			for (Node node : children) {
				total += node.getTotal();
			}

			return total;
		}

		public Node getParent() {
			return parent;
		}

		public List<Node> getChildren() {
			return children;
		}

		public Node addChild(Node child) {
			child.parent = this;
			children.add(child);

			if (type == null || type.toString().length() == 0 || type == "leaf") {
				type = "folder";
			}

			return this;
		}

		public void removeChild(Node c) {
			children.remove(c);
			if (c.getParent() == this) {
				c.parent = null;
			}
		}

		public int getSize() {
			if (children == null) {
				return 0;
			}
			return children.size();
		}

		public Serializable getType() {
			if (this.getSize() == 0 && type == "folder") {
				return "leaf";
			}
			return type;
		}

		public Node setType(Serializable type) {
			this.type = type;

			return this;
		}

		public Serializable getId() {
			return id;
		}

		// public void setId(String id) {
		// this.id = id;
		// }

		public String getName() {
			return name;
		}

		public Node setName(String name) {
			this.name = name;

			return this;
		}

		// public String getAction() {
		// return action;
		// }
		//
		// public void setAction(String expr) {
		// this.action = expr;
		// }

		public Object getParams() {
			return params;
		}

		public Node setParams(Object value) {
			this.params = value;

			return this;
		}

		public String getStatus() {
			return status;
		}

		public Node setStatus(String status) {
			this.status = status;

			return this;
		}

		public String getDesc() {
			return desc;
		}

		public Node setDesc(String desc) {
			if (desc != null)
				this.desc = desc;

			return this;
		}

		public String getIcon() {
			return icon;
		}

		public Node setIcon(String icon) {
			this.icon = icon;

			return this;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public Map getDynaProp() {
			return dynamicProps;
		}

		public void setDynaProp(Properties props) {
			dynamicProps = props;
		}

		public String getString(String key) {
			Object obj = dynamicProps.get(key);
			return obj == null ? null : obj.toString();
		}

		public Object get(String key) {
			return dynamicProps.get(key);
		}

		public void set(String key, Object value) {
			if (value == null) {
				dynamicProps.remove(key);
			} else {
				dynamicProps.put(key, value);
			}
		}

		protected void set(byte index, boolean flag) {
			int MASK = new Double(Math.pow(2, new Double(index - 1))).intValue();
			if (maskValue == null) {
				maskValue = 0;
			}
			synchronized (maskValue) {
				if (flag) {// 隐藏
					maskValue |= MASK;
				} else {// 不隐藏
					int mask = ~MASK;// 反码
					maskValue &= mask;// 与
				}
			}
		}

		public boolean is(byte index) {
			int MASK = new Double(Math.pow(2, new Double(index - 1))).intValue();
			if (maskValue == null) {
				return false;
			}
			synchronized (maskValue) {
				return (maskValue & MASK) > 0;
			}
		}

		public String toString() {
			return this.name;
		}

		@Override
		public int hashCode() {
			if (id == null) {
				return super.hashCode();
			}
			return 37 * 17 + id.hashCode();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;

			if (!Node.class.isAssignableFrom(that.getClass())) {
				return false;
			}

			Node thatEntity = (Node) that;
			if (id == null || thatEntity.id == null) {
				return this == that;
			}

			return thatEntity.id.equals(id);
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer orderby) {
			this.order = orderby;
		}
	}

	public void order() {
		order(children);
	}

	private void order(List<Node> list) {
		if (list == null) {
			return;
		}
		SortUtils.sort(list, "order", true);
		for (Node node : list) {
			order(node.getChildren());
		}
	}

}
