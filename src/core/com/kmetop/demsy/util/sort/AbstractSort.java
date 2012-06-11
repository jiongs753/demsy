package com.kmetop.demsy.util.sort;

import java.util.List;

public abstract class AbstractSort implements SortStrategy {
	public void sort(List list, String field, boolean nullGT) {
		if (list == null) {
			return;
		}
		Object[] array = list.toArray();
		this.sort(array, field, nullGT);
		list.clear();
		for (Object obj : array) {
			list.add(obj);
		}
	}
}
