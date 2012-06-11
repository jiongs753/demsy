package com.kmetop.demsy.orm.nutz.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.nutz.lang.ComboException;
import org.nutz.trans.Transaction;

public class DemsyTransaction extends Transaction {

	private static int ID = 0;

	private List<Pair> list;

	private static class Pair {
		Pair(DataSource ds, Connection conn, int level) throws SQLException {
			this.ds = ds;
			this.conn = conn;
			oldLevel = conn.getTransactionIsolation();
			if (oldLevel != level)
				conn.setTransactionIsolation(level);
		}

		DataSource ds;

		Connection conn;

		int oldLevel;
	}

	public DemsyTransaction() {
		list = new ArrayList<Pair>();
	}

	@Override
	protected void commit() {
		ComboException ce = new ComboException();
		for (Pair p : list) {
			try {
				// 提交事务
				p.conn.commit();
				// 恢复旧的事务级别
				if (p.conn.getTransactionIsolation() != p.oldLevel)
					p.conn.setTransactionIsolation(p.oldLevel);
			} catch (SQLException e) {
				ce.add(e);
			}
		}
		// 如果有一个数据源提交时发生异常，抛出
		if (null != ce.getCause()) {
			throw ce;
		}
	}

	@Override
	public Connection getConnection(DataSource dataSource) throws SQLException {
		for (Pair p : list)
			if (p.ds == dataSource)
				return p.conn;

		Connection conn = dataSource.getConnection();
		// System.out.printf("=> %s\n", conn.toString());
		if (conn.getAutoCommit())
			conn.setAutoCommit(false);
		// Store conn, it will set the trans level
		list.add(new Pair(dataSource, conn, getLevel()));

		return conn;
	}

	@Override
	public int getId() {
		return ID++;
	}

	@Override
	public void close() {
		ComboException ce = new ComboException();
		for (Pair p : list) {
			try {
				// 试图恢复旧的事务级别
				if (!p.conn.isClosed())
					if (p.conn.getTransactionIsolation() != p.oldLevel)
						p.conn.setTransactionIsolation(p.oldLevel);
			} catch (Throwable e) {
			} finally {
				try {
					p.conn.close();
				} catch (Exception e) {
					ce.add(e);
				}
			}
		}
		// 清除数据源记录
		list.clear();
	}

	@Override
	protected void rollback() {
		for (Pair p : list) {
			try {
				p.conn.rollback();
			} catch (Throwable e) {
			}
		}
	}

}
