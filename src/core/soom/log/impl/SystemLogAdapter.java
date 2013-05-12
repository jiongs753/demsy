package soom.log.impl;

import soom.log.Log;
import soom.log.LogAdapter;

public class SystemLogAdapter implements LogAdapter {

	public Log getLogger(String className) {
		return SystemLog.me();
	}

	public boolean canWork() {
		return true;
	}

}
