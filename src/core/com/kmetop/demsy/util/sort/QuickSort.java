package com.kmetop.demsy.util.sort;

import com.kmetop.demsy.lang.Obj;

/**
 * 快速排序，要求待排序的数组必须实现Comparable接口
 */
public class QuickSort extends AbstractSort {
	private static final int CUTOFF = 3; // 当元素数大于此值时采用快速排序

	private String sortField;

	/**
	 * 利用快速排序算法对数组obj进行排序，要求待排序的数组必须实现了Comparable接口
	 */
	public void sort(Object[] obj, String field, boolean nullGT) {
		if (obj == null) {
			throw new NullPointerException("The argument can not be null!");
		}
		this.sortField = field;
		quickSort(obj, 0, obj.length - 1, nullGT);
	}

	/**
	 * 对数组obj快速排序
	 * 
	 * @param obj
	 *            待排序的数组
	 * @param left
	 *            数组的下界
	 * @param right
	 *            数组的上界
	 */
	private void quickSort(Object[] obj, int left, int right, boolean nullGT) {
		if (left + CUTOFF > right) {
			SortStrategy ss = new ChooseSort();
			ss.sort(obj, this.sortField, nullGT);
		} else {
			// 找出枢轴点，并将它放在数组最后面的位置
			pivot(obj, left, right, nullGT);
			int i = left, j = right - 1;
			Object tmp = null;
			while (true) {
				// 将i, j分别移到大于/小于枢纽值的位置
				// 因为数组的第一个和倒数第二个元素分别小于和大于枢纽元，所以不会发生数组越界
				while (true) {
					try {
						if (SortUtils.compare(Obj.getValue(obj[++i], sortField), Obj.getValue(obj[right - 1], sortField), nullGT) < 0) {
						} else {
							break;
						}
					} catch (Throwable ex) {
						break;
					}
				}

				while (true) {
					try {
						if (SortUtils.compare(Obj.getValue(obj[--j], sortField), Obj.getValue(obj[right - 1], sortField), nullGT) < 0) {
						} else {
							break;
						}
					} catch (Throwable ex) {
						break;
					}
				}
				// 交换
				if (i < j) {
					tmp = obj[i];
					obj[i] = obj[j];
					obj[j] = tmp;
				} else {
					break;
				}
			}
			// 将枢纽值与i指向的值交换
			tmp = obj[i];
			obj[i] = obj[right - 1];
			obj[right - 1] = tmp;
			// 对枢纽值左侧和右侧数组继续进行快速排序
			quickSort(obj, left, i - 1, nullGT);
			quickSort(obj, i + 1, right, nullGT);
		}
	}

	/**
	 * 在数组obj中选取枢纽元，选取方法为取数组第一个、中间一个、最后一个元素中中间的一个。将枢纽元置于倒数第二个位置，三个中最大的放在数组最后一个位置
	 * ，最小的放在第一个位置
	 * 
	 * @param obj
	 *            要选择枢纽元的数组
	 * @param left
	 *            数组的下界
	 * @param right
	 *            数组的上界
	 */
	private void pivot(Object[] obj, int left, int right, boolean nullGT) {
		int center = (left + right) / 2;
		Object tmp = null;

		try {
			if (SortUtils.compare(Obj.getValue(obj[left], sortField), Obj.getValue(obj[center], sortField), nullGT) > 0) {
				tmp = obj[left];
				obj[left] = obj[center];
				obj[center] = tmp;
			}
		} catch (Throwable ex) {
		}
		try {
			if (SortUtils.compare(Obj.getValue(obj[left], sortField), Obj.getValue(obj[right], sortField), nullGT) > 0) {
				tmp = obj[left];
				obj[left] = obj[right];
				obj[right] = tmp;
			}
		} catch (Throwable ex) {
		}
		try {
			if (SortUtils.compare(Obj.getValue(obj[center], sortField), Obj.getValue(obj[right], sortField), nullGT) > 0) {
				tmp = obj[center];
				obj[center] = obj[right];
				obj[center] = tmp;
			}
		} catch (Throwable ex) {
		}

		// 将枢纽元置于数组的倒数第二个
		tmp = obj[center];
		obj[center] = obj[right - 1];
		obj[right - 1] = tmp;
	}
}
