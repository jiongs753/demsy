package com.kmetop.demsy.security;

import java.io.IOException;

import com.kmetop.demsy.comlib.security.IUser;

public interface IRootUserFactory {

	IUser getUser(String username);

	void saveUser(IUser login) throws IOException;
}
