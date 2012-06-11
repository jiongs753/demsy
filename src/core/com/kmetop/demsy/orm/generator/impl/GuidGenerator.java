package com.kmetop.demsy.orm.generator.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.orm.NoTransConnCallback;
import com.kmetop.demsy.orm.generator.Generator;
import com.kmetop.demsy.orm.nutz.IExtDao;

public class GuidGenerator implements Generator {
	private Map<IExtDao, String> sqls = new HashMap();

	private String sql(IExtDao dao) {
		if (sqls.get(dao) == null) {
			sqls.put(dao, dao.getDialect().getSelectGUIDString());
		}
		return sqls.get(dao);
	}

	private Serializable generate(IExtDao dao, Connection conn, Object object) throws SQLException {
		try {
			PreparedStatement st = conn.prepareStatement(sql(dao));
			try {
				ResultSet rs = st.executeQuery();
				final String result;
				try {
					rs.next();
					result = rs.getString(1);
				} finally {
					rs.close();
				}
				return result;
			} finally {
				st.close();
			}
		} catch (SQLException sqle) {
			throw sqle;
		}
	}

	public Serializable generate(final IExtDao dao, final Object object) {
		return (Serializable) dao.run(new NoTransConnCallback() {
			public Object invoke(Connection conn) throws Exception {
				Serializable id = generate(dao, conn, object);
				return id;
			}
		});
	}

}
