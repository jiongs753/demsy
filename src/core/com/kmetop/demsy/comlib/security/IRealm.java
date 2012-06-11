package com.kmetop.demsy.comlib.security;

import com.kmetop.demsy.comlib.entity.IBizComponent;

public interface IRealm extends IBizComponent {
	IModule getUserModule();

}
