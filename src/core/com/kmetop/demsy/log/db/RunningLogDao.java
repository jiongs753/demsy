package com.kmetop.demsy.log.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.impl.base.log.RunningLog;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.NoTransConnCallback;
import com.kmetop.demsy.orm.dialect.Dialect;
import com.kmetop.demsy.orm.generator.INamingStrategy;

public class RunningLogDao {

	private static RunningLogDao me;

	private IOrm orm;

	public static RunningLogDao me() {
		synchronized (RunningLogDao.class) {
			if (me == null) {
				try {
					me = new RunningLogDao();
				} catch (Throwable e) {
				}
			}
		}
		return me;
	}

	private RunningLogDao() {
		orm = Demsy.orm();
	}

	public synchronized void save(final RunningLog log) throws SQLException {
		if (orm != null)
			orm.run(new NoTransConnCallback() {
				public Object invoke(Connection conn) throws Exception {
					Dialect dialect = orm.getDialect();
					INamingStrategy naming = orm.getNamingStrategy();
					String pk = naming.columnName("_id");
					String tableName = naming.classToTableName(RunningLog.class.getSimpleName());
					String insertSql = sqlInsertStrings(dialect, naming, tableName, pk);
					String maxIdSql = "SELECT MAX(" + pk + ") FROM " + tableName;

					Statement stat = null;
					ResultSet rs = null;
					long maxId = 0;
					try {
						stat = conn.createStatement();
						rs = stat.executeQuery(maxIdSql);
						if (rs.next())
							maxId = rs.getLong(1);
					} catch (SQLException e) {
						//System.err.println("执行SQL<" + maxIdSql + ">出错! 错误信息： " + e);
					} finally {
						if (null != stat)
							try {
								stat.close();
							} catch (Throwable e) {
							}
						if (null != rs)
							try {
								rs.close();
							} catch (Throwable e) {
							}
					}
					PreparedStatement stmt = conn.prepareStatement(insertSql);
					try {
						stmt.setLong(1, maxId + 1);
						stmt.setString(2, log.getLoginuser());
						stmt.setString(3, log.getFqnofctgrcls());
						stmt.setString(4, log.getLoggername());
						stmt.setTimestamp(5, new Timestamp(log.getDatetime().getTime()));
						stmt.setString(6, log.getLevel());
						stmt.setString(7, log.getMessage());
						stmt.setString(8, log.getThreadname());
						stmt.setString(9, log.getStacktrace());
						stmt.setString(10, log.getNdc());
						stmt.setString(11, log.getLocationinfo());
						stmt.setString(12, log.getRemoteUrl());
						stmt.setString(13, log.getRemoteUri());
						stmt.setString(14, log.getRemoteIp());
						stmt.setLong(15, log.getMemEslipse());
						stmt.setLong(16, log.getEslipse());
						stmt.setString(17, log.getMonitor());

						return stmt.execute();
					} catch (SQLException sqle) {
						//System.err.println("执行SQL<" + insertSql + ">出错! 错误信息： " + sqle);
						return null;
					} finally {
						if (null != stmt)
							try {
								stmt.close();
							} catch (Throwable e) {
							}
					}
				}
			});
	}

	private String sqlInsertStrings(Dialect dialect, INamingStrategy naming, String tableName, String pk) {
		StringBuffer sb = new StringBuffer().append("insert into ").append(tableName).append("(");
		sb.append(pk);
		sb.append(",").append(naming.propertyToColumnName("loginuser"));
		sb.append(",").append(naming.propertyToColumnName("fqnofctgrcls"));
		sb.append(",").append(naming.propertyToColumnName("loggername"));
		sb.append(",").append(naming.propertyToColumnName("datetime"));
		sb.append(",").append(naming.propertyToColumnName("level"));
		sb.append(",").append(naming.propertyToColumnName("message"));
		sb.append(",").append(naming.propertyToColumnName("threadname"));
		sb.append(",").append(naming.propertyToColumnName("stacktrace"));
		sb.append(",").append(naming.propertyToColumnName("ndc"));
		sb.append(",").append(naming.propertyToColumnName("locationinfo"));
		sb.append(",").append(naming.propertyToColumnName("remoteUrl"));
		sb.append(",").append(naming.propertyToColumnName("remoteUri"));
		sb.append(",").append(naming.propertyToColumnName("remoteIp"));
		sb.append(",").append(naming.propertyToColumnName("memEslipse"));
		sb.append(",").append(naming.propertyToColumnName("eslipse"));
		sb.append(",").append(naming.propertyToColumnName("monitor"));
		sb.append(") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		return sb.toString();
	}
	//
	// private String sqlCreateStrings(Dialect dialect, INamingStrategy naming,
	// String tableName, String pk) throws DemsyException {
	// StringBuffer sb = new
	// StringBuffer().append(dialect.getCreateTableString()).append(' ').append(tableName).append("(");
	// sb.append(pk).append(' ');
	// sb.append(dialect.getTypeName(Types.BIGINT, 0, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("loginuser")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 50, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("fqnofctgrcls")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("loggername")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("datetime")).append(' ');
	// sb.append(dialect.getTypeName(Types.DATE, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("level")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 20, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("message")).append(' ');
	// sb.append(dialect.getTypeName(Types.CLOB, 0, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("threadname")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("stacktrace")).append(' ');
	// sb.append(dialect.getTypeName(Types.CLOB, 0, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("ndc")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("locationinfo")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	// sb.append(",").append(naming.propertyToColumnName("remoteUrl")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	// sb.append(",").append(naming.propertyToColumnName("remoteUri")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	// sb.append(",").append(naming.propertyToColumnName("remoteIp")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 64, 0, 0));
	//
	// sb.append(", primary key ( ").append(naming.columnName("_id")).append(" ) ) ");
	//
	// return sb.toString();
	// }
}
