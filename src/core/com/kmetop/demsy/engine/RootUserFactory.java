package com.kmetop.demsy.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.entity.base.BaseUser;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.config.IConfig;
import com.kmetop.demsy.config.impl.BaseConfig;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.security.IRootUserFactory;

public class RootUserFactory extends BaseConfig implements IRootUserFactory {
	public RootUserFactory() {
		super(Demsy.appconfig.getUserConfig());
	}

	private Map<String, BaseUser> userMap = new HashMap();

	public IUser getUser(final String username) {
		final String password = this.get(username);
		if (password == null) {
			return null;
		}
		BaseUser user = userMap.get(username);
		if (user == null) {
			user = new BaseUser() {
				public boolean isBuildin() {
					return true;
				}
			};
			user.setUsername(username);
			user.setPassword(password);

			userMap.put(username, user);
		}
		return user;
	}

	public void saveUser(IUser login) throws IOException {
		if (login != null) {
			IUser user = this.getUser(login.getUsername());
			if (user != null) {
				String pwd = login.getPassword();
				if (!Str.isEmpty(pwd)) {
					this.put(login.getUsername(), pwd);
					this.save();
				}
			}
		}
	}

	@Override
	public IConfig copy() {
		return null;
	}
}
