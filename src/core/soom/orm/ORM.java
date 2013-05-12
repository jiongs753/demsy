package soom.orm;

/**
 * 对象关系数据库映射接口：用于管理和查询数据库数据表，包括：
 * <UL>
 * <LI>创建、删除、修改数据库表、字段等；
 * <LI>增、删、查、改数据库表记录；
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public interface ORM {
	/**
	 * 查询“自定义系统”中的满足条件的数据集。
	 * 
	 * @param systemCode
	 *            自定义系统编码
	 * @param expr
	 *            查询表达式
	 * @return 满足条件的数据集
	 */
	public <T> ORMResult<T> query(String systemCode, ORMExpr expr);

	/**
	 * 查询“自定义系统”中满足条件的第一条数据。
	 * 
	 * @param systemCode
	 *            业务系统编码
	 * @param expr
	 *            查询表达式
	 * @return 满足条件的第一条数据
	 */
	public <T> Object get(String systemCode, ORMExpr expr);
}
