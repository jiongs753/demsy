package com.kmetop.demsy.comlib.biz;

import com.kmetop.demsy.comlib.entity.IBizComponent;

/**
 * 自定义实体字段
 * 
 * @author yongshan.ji
 */
public interface IBizField extends IBizComponent {

	/**
	 * 获取业务系统
	 * 
	 * @return
	 */
	IBizSystem getSystem();

	/**
	 * 获取字段所属的分组
	 * 
	 * @return
	 */
	IBizFieldGroup getFieldGroup();

	/**
	 * 获取字段类型
	 * 
	 * @return
	 */
	IBizFieldType getType();

	/**
	 * 获取“系统引用”类型的字段引用到哪个系统？
	 * 
	 * @return
	 */
	IBizSystem getRefrenceSystem();

	/**
	 * 获取“字段引用”类型的字段引用到哪个字段？
	 * 
	 * @return
	 */
	IBizField getRefrenceField();

	String getRefrenceFields();

	IBizField getParent();

	/**
	 * 用在“数值类型”字段中，用来表示小数点位数。
	 * 
	 * @return
	 */
	Integer getScale();

	/**
	 * 用在“数值类型”和“字符串类型”中，表示整数位数或的字符长度。
	 * 
	 * @return
	 */
	Integer getPrecision();

	/**
	 * <b>字段显示模式(mode)：</b>用空格分隔，与子系统数据操作中指定的动作模式组合使用。
	 * <p>
	 * 格式：[操作模式:显示模式]
	 * <ul>
	 * <li>M: Must 必需的</li>
	 * <li>E: Edit 可编辑的 (即可读写)</li>
	 * <li>I: Inspect 检查（带有一个隐藏字段存放其值）</li>
	 * <li>S: Show 显示（但不带隐藏字段）</li>
	 * <li>N: None 不显示</li>
	 * <li>P: Present 如果该字段有值就显示，否则如果没有值就不显示该字段</li>
	 * <li>H: Hidden 隐藏 (不显示，但有一个隐藏框存在)</li>
	 * <li>R: Read only 只读</li>
	 * <li>D: Disable 禁用</li>
	 * </ul>
	 * <p>
	 * <b> 字段显示模式举例说明(mode)： </b>
	 * <ul>
	 * <li>v:I——查看数据时，该字段处于检查模式</li>
	 * <li>e:E——编辑数据时，字段可编辑</li>
	 * <li>bu:N——批量修改数据时，字段不可见</li>
	 * </ul>
	 */
	String getMode();

	/**
	 * 获取“数值类型”字段值计量单位选项，以便录入、编辑数据过程中生成计量单位下拉选项。
	 * <p>
	 * 计量单位选项可以是计量单位类别编号，如距离“distance”；或如下个格式的文本：{"m":"米", "km":"千米"}
	 * 
	 * @return
	 */
	String getUomOptions();

	/**
	 * 获取“数值类型”字段计量单位，以便录入编辑数据过程中固定计量单位
	 * <p>
	 * 如果计量单位选项是计量单位类别，则该值可以是指定类别下的计量单位编号。
	 * <p>
	 * 如计量单位使用的是类别编号“distance”(距离)，则计量单位可以限制为“m”(米)。
	 * <p>
	 * 
	 * 
	 * @return
	 */
	String getUomOption();

	/**
	 * 获取“字典类型”字段值选项，以便录入、编辑数据过程中生成单选或多选选项。
	 * <p>
	 * 字段值选项可以是字典类别编号，或如下个格式的文本：{"m":"男", "w":"女"}
	 * 
	 * @return
	 */
	String getOptions();

	/**
	 * 获取“文本类型”、“数值类型”类型字段的掩码，即数据格式必须满足正则表达式掩码规则。
	 * <p>
	 * 同时用来校验UI上录入的数据
	 * <p>
	 * 如：邮箱、电话号码、邮政编码等
	 * 
	 * @return
	 */
	String getRegexpMask();

	/**
	 * 默认值表达式
	 * 
	 * @return
	 */
	String getDefaultValue();

	/**
	 * 获取“数值类型”、“日期”、“日期时间”类型字段的显示格式。
	 * <p>
	 * 日期类型如：yyyy-MM-dd(2010-12-31)；(yyyyMMdd)20101231；(yyyy-MM-dd
	 * hh:mm:ss)2010-12-31 12:30:25
	 * <p>
	 * 数字类型如：###,###.00
	 * 
	 * @return
	 */
	String getPattern();

	/**
	 * 获取动态生成的业务实体属性名。
	 * 
	 * @return
	 */
	String getPropName();

	void setPropName(String prop);

	/**
	 * 获取上传类型：上传字段支持的文件类型
	 * 
	 * @return
	 */
	public String getUploadType();

	/**
	 * 判断字段是否为临时字段？临时字段对应的数据不会被持久化到数据库中。
	 * 
	 * @return
	 */
	boolean isTransientField();

	/**
	 * 判断该字段是否允许在列表中显示？
	 * 
	 * @return
	 */
	boolean isGridField();

	int getGridOrder();

	/**
	 * 判断“系统引用”类型的字段是否允许将该系统映射成引用系统的子系统？
	 * 
	 * @return
	 */
	boolean isMappingToMaster();

	/**
	 * 判断“系统引用”类型的字段是否以Combobox方式产生字段UI？
	 * 
	 * @return
	 */
	boolean isSysCombobox();

	/**
	 * 判断是否以密码方式录入字段值
	 * 
	 * @return
	 */
	boolean isPassword();

	/**
	 * 判断“字段引用”类型的字段是否支持数据冗余？
	 * 
	 * @return
	 */
	boolean isSysRedundancy();

	/**
	 * 判断“系统引用”类型的字段是否支持多选？
	 * 
	 * @return
	 */
	boolean isSysMultiple();

	boolean isDisabledNavi();

	String getUiTemplate();

	int getGridWidth();

	/**
	 * 级联模式：即哪些字段发生变化后，该字段将以何种模式显示。
	 * <p>
	 * 格式: {上级字段值}:{mode}
	 * 
	 * @return
	 */
	String getCascadeMode();

	boolean isGroupBy();

	void setCascadeMode(String cascadeMode);

	void setOptions(String options);

	void setName(String name);

	boolean isPrivacy();

	void setMode(String mode);

	void setType(IBizFieldType fieldType);

}
