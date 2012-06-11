package com.kmetop.demsy.util.sort;

import com.kmetop.demsy.lang.Obj;

public class MergeSort extends AbstractSort {
	private Object[] bridge;

	private String sortField;

	public void sort(Object[] obj, String field, boolean nullGT) {
		if (obj == null) {
			throw new NullPointerException("The param can not be null!");
		}
		this.sortField = field;
		bridge = new Object[obj.length]; // ��ʼ���м�����
		mergeSort(obj, 0, obj.length - 1, nullGT); // �鲢����
		bridge = null;
	}

	private void mergeSort(Object[] obj, int left, int right, boolean nullGT) {
		if (left < right) {
			int center = (left + right) / 2;
			mergeSort(obj, left, center, nullGT);
			mergeSort(obj, center + 1, right, nullGT);
			merge(obj, left, center, right, nullGT);
		}
	}

	private void merge(Object[] obj, int left, int center, int right, boolean nullGT) {
		int mid = center + 1;
		int third = left;
		int tmp = left;
		while (left <= center && mid <= right) {
			try {
				if (SortUtils.compare(Obj.getValue(obj[left], sortField), Obj.getValue(obj[mid], sortField), nullGT) <= 0) {
					bridge[third++] = obj[left++];
				} else {
					bridge[third++] = obj[mid++];
				}
			} catch (Throwable ex) {
				bridge[third++] = obj[mid++];
			}
		}
		while (mid <= right) {
			bridge[third++] = obj[mid++];
		}
		while (left <= center) {
			bridge[third++] = obj[left++];
		}
		copy(obj, tmp, right);
	}

	private void copy(Object[] obj, int left, int right) {
		while (left <= right) {
			obj[left] = bridge[left];
			left++;
		}
	}
}
