package com.kmetop.demsy.biz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.kmetop.demsy.lang.DemsyException;

/**
 * 业务系统数据源：数据源可以是EXCEL、XML、TXT等。
 * 
 * @author yongshan.ji
 * 
 */
public interface IBizDataSource<SYSTEM> {
	/**
	 * 解析excel文件并转换成业务系统模型，返回实体结果集。
	 * <OL>
	 * <LI>EXCEL数据表格表头各列名称必须和系统字段名称对应，与表头顺序无关；
	 * <LI>对于系统引用字段和字典字段，EXCEL相关列的可选值必须在“基础模块”或“字典”中存在；
	 * <LI>建议用不会发生变化、能唯一定位一条记录的字段作为主键列（如：帐号字段），EXCEL数据表主键值不存在或为空表示添加一条记录；
	 * <LI>EXCEL数据表格除了表头和数据行外，其他任何多余的行必须删除，否则导入过程中会抛错；
	 * <LI>目前支持导入的数据类型有：文本、日期、boolean、数字、字典(单选)、系统引用(单选)；
	 * <LI>多选字段目前不支持数据导入（如：字典类型的多选和系统引用的多选均不支持）；
	 * <LI>EXCEL数据表格的第一行必须为表头，从第2行开始为数据；
	 * </OL>
	 * 
	 * @param system
	 *            业务系统对象
	 * @param file
	 *            excel文件
	 * @param pkNames
	 *            主键字段名称
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws DemsyException
	 */
	List getFromExcel(SYSTEM system, File file, String[] pkNames) throws FileNotFoundException, IOException, DemsyException;
}
