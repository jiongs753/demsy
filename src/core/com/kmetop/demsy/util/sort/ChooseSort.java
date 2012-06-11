package com.kmetop.demsy.util.sort;

import com.kmetop.demsy.lang.Obj;

/**
 * 利用选择排序法对数组排序，数组中元素必须实现了Comparable接口。
 */
public class ChooseSort extends AbstractSort {
	/**
	 * 对数组obj中的元素以选择排序算法进行排序
	 */
	public void sort(Object[] obj, String field, boolean nullGT) {
		if (obj == null) {
			throw new NullPointerException("The argument can not be null!");
		}

		Object tmp = null;
		int index = 0;
		for (int i = 0; i < obj.length - 1; i++) {
			index = i;
			tmp = obj[i];
			Object valueTmp = null;
			try {
				valueTmp = Obj.getValue(tmp, field);
			} catch (Throwable ex) {
				continue;
			}
			for (int j = i + 1; j < obj.length; j++) {
				// 对邻接的元素进行比较，如果后面的小，就记下它的位置
				Object value = null;
				try {
					value = Obj.getValue(obj[j], field);
				} catch (Throwable ex) {
					continue;
				}
				try {
					if (SortUtils.compare(valueTmp, value, nullGT) > 0) {
						tmp = obj[j]; // 要每次比较都记录下当前小的这个值!
						index = j;
					}
				} catch (Throwable ex) {
				}
			}
			// 将最小的元素交换到前面
			tmp = obj[i];
			obj[i] = obj[index];
			obj[index] = tmp;
		}
	}
}
