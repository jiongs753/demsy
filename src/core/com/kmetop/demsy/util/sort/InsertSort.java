package com.kmetop.demsy.util.sort;

import com.kmetop.demsy.lang.Obj;

/**
 * 插入排序，要求待排序的数组必须实现Comparable接口
 */
public class InsertSort extends AbstractSort {
	private String sortField;

	/**
	 * 利用插入排序算法对obj进行排序
	 */
	public void sort(Object[] obj, String field, boolean nullGT) {
		if (obj == null) {
			throw new NullPointerException("The argument can not be null!");
		}
		this.sortField = field;
		/*
		 * 对数组中的第i个元素，认为它前面的i - 1个已经排序好，然后将它插入到前面的i - 1个元素中
		 */
		int size = 1;
		while (size < obj.length) {
			insert(obj, size++, obj[size - 1], nullGT);
		}
	}

	/**
	 * 在已经排序好的数组中插入一个元素，使插入后的数组仍然有序
	 * 
	 * @param obj
	 *            已经排序好的数组
	 * @param size
	 *            已经排序好的数组的大小
	 * @param c
	 *            待插入的元素
	 */
	private void insert(Object[] obj, int size, Object c, boolean nullGT) {
		for (int i = 0; i < size; i++) {
			try {
				Object value1 = c;
				Object value2 = obj[i];
				if (sortField != null && sortField.trim().length() > 0) {
					value1 = Obj.getValue(value1, sortField);
					value2 = Obj.getValue(value2, sortField);
				}
				if (SortUtils.compare(value1, value2, nullGT) < 0) {
					// 如果待插入的元素小于当前元素，则把当前元素后面的元素依次后移一位
					for (int j = size; j > i; j--) {
						obj[j] = obj[j - 1];
					}
					obj[i] = c;
					break;
				}
			} catch (Throwable ex) {
			}
		}
	}
}
