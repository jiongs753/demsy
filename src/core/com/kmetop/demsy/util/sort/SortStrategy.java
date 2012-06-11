package com.kmetop.demsy.util.sort;

import java.util.List;

public interface SortStrategy {
	public void sort(Object[] obj, String field, boolean nullGT);

	public void sort(List list, String field, boolean nullGT);
}
