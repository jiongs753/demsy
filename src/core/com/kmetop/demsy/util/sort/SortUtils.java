package com.kmetop.demsy.util.sort;

import java.util.Date;
import java.util.List;

public abstract class SortUtils {
	private static final SortStrategy sort = new InsertSort();

	public static double compare(Object obj1, Object obj2, boolean nullGT) {
		if (obj1 == null && obj2 == null) {
			return 0;
		} else if (obj1 == null) {
			if (nullGT)
				return 1;
			return -1;
		} else if (obj2 == null) {
			if (nullGT)
				return -1;
			return 1;
		}
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return ((Number) obj1).doubleValue() - ((Number) obj2).doubleValue();
		}
		if (obj1 instanceof Date && obj2 instanceof Date) {
			return ((Date) obj1).getTime() - ((Date) obj2).getTime();
		}
		return obj1.toString().compareTo(obj2.toString());
	}

	public static void sort(Object[] obj, boolean nullGT) {
		sort.sort(obj, "orderby", nullGT);
	}

	public static void sort(List obj, boolean nullGT) {
		sort.sort(obj, "orderby", nullGT);
	}

	public static void sort(Object[] obj, String field, boolean nullGT) {
		sort.sort(obj, field, nullGT);
	}

	public static void sort(List list, String field, boolean nullGT) {
		sort.sort(list, field, nullGT);
	}
}
