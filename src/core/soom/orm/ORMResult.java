package soom.orm;

import java.util.List;

/**
 * 该类的对象用于包装数据库查询结果
 * 
 * @author yongshan.ji
 * 
 */
public class ORMResult<T> {
	/**
	 * 总记录数
	 */
	protected int total;

	/**
	 * 页大小
	 */
	protected int pageSize;

	/**
	 * 也索引
	 */
	protected int pageIndex;

	/**
	 * 查询到的数据库记录
	 */
	protected List<T> data;
}
