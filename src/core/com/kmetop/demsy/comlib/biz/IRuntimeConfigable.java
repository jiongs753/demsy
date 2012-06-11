package com.kmetop.demsy.comlib.biz;

import com.kmetop.demsy.comlib.biz.field.FakeSubSystem;

/**
 * 运行时可配置接口： 即可以在运行时自定义新字段或重新设置原有的物理字段
 * 
 * @author yongshan.ji
 * 
 */
public interface IRuntimeConfigable {
	/**
	 * 描述属性配置的继承关系
	 * 
	 * @return
	 */
	IRuntimeConfigable getParent();

	/**
	 * 获取动态特性。
	 * <p>
	 * 自定义特性格式：为JSON格式的数组，数组中的每个元素为一个属性的描述
	 * <p>
	 * [{type:{code:"String"}, propName:"prop1",
	 * name:"标题",options="1:男 0:女"},{...},...]
	 * <p>
	 * type: 字段类型编号可以是 String RichText Integer Double Date Boolean Upload Byte
	 * Short Long Character BigInteger
	 * 
	 * @return
	 */
	FakeSubSystem<? extends IBizField> getCustomFields();

}
