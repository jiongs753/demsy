package com.kmetop.demsy.comlib.biz;

import com.kmetop.demsy.comlib.entity.IBizComponent;

/**
 * 实体模块组件
 * 
 * @author yongshan.ji
 */
public interface IBizSystem extends IBizComponent {
	public static final byte IGLORE_SECURITY = 1;

	IBizCatalog getCatalog();

	/**
	 * 业务模块扩展类全名
	 * 
	 * @return
	 */
	String getExtendClass();

	/**
	 * 获取功能模块“数据实体”拥有者关联的字段，用来控制功能模块“数据实体”安全。
	 * <ul>
	 * <li>数据实体拥有者字段可以是组或用户类型的字段。也可以用"|"分隔的字段编号描述多字段</li>
	 * <li>如果数据实体拥有类型为用户，则该字段中的值与特定的用户相关联，表示模块数据实体的拥有者为特定的用户。</li>
	 * <li>如果数据实体拥有类型为组，则该字段中的值与特定的组相关联，表示模块数据实体的拥有者为特定的组。</li>
	 * </ul>
	 * 
	 * @return 字段名称
	 */
	String getEntityOwnerField();

	/**
	 * 映射实体表， 如果未指定映射表，则自动计算映射表名称
	 * <p>
	 * 表示实体模块组件被映射到哪个实体表？
	 * <p>
	 * 如果指定的实体表不存在或空，则使用指定的值作为实体表名称并创建相关的实体表。
	 * <p>
	 * 如果指定的实体表已存在，则自动根据表属性创建实体模块组件。
	 * <p>
	 * 一般用于实体表已经存在，或无需对实体表进行编码的情况。
	 */
	String getMappingTable();

	/**
	 * 映射实体类，如果为指定映射实体类，择将自动生成映射实体类
	 * <p>
	 * 表示实体模块组件被映射到哪个实体类？
	 * <p>
	 * 如果指定的实体类不存在或空，则使用指定的值作为实体类名称并创建相关的实体类。
	 * <p>
	 * 如果指定的实体类已存在，则自动根据类属性创建实体模块组件。
	 * <p>
	 * 一般用于实体类已经存在，或无需对实体表进行编码的情况。
	 * 
	 * @return
	 */
	String getMappingClass();

	String getTemplate();

	String getVersion();

	void setVersion(String v);

	Integer getOrderby();

	void setCode(String code);

	byte getLayout();

}
