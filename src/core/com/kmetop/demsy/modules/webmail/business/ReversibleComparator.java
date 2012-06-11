package com.kmetop.demsy.modules.webmail.business;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("serial")
public abstract class ReversibleComparator<T> implements Comparator<T>, Serializable {

	protected boolean reverse = false;

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

}
