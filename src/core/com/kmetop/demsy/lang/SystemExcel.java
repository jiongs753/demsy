package com.kmetop.demsy.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.IBizSession;
import com.kmetop.demsy.comlib.IBizEngine;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.entity.IBizEntity;
import com.kmetop.demsy.comlib.security.IAction;
import com.kmetop.demsy.orm.expr.Expr;

/**
 * 子系统Excel导入导出工具库:
 * 
 * @author yongshan.ji
 * 
 */
public class SystemExcel extends Excel {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	private IBizEngine bizEngine;

	private Class systemClass;
	private Map<String, IBizField> fields = new HashMap();// <字段名称, 字段>

	private String[] excelHeads;
	private List<String[]> excelRows;

	public SystemExcel(IBizSystem system, IAction action, File excel) throws FileNotFoundException, IOException,
			DemsyException {
		this.bizEngine = Demsy.bizEngine;
		systemClass = bizEngine.getType(system);

		// 处理自定义系统相关信息
		List<IBizField> datas = (List<IBizField>) bizEngine.getFieldsOfEnabled(system);
		for (IBizField data : datas) {
			String name = data.getName();
			boolean valid = !data.isDisabled();
			String mode = bizEngine.getMode(data, action, false, "E");
			valid = valid && (mode.equals("E") || mode.equals("M"));

			if (valid)
				fields.put(name, data);
		}
		if (log.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("系统(" + system.getName() + ")字段：\n");
			for (IBizField fld : datas) {
				if (fields.get(fld.getName()) == null) {
					continue;
				}
				sb.append(fld.getName()).append("：").append(bizEngine.getPropName(fld));
				sb.append("\n");
			}
			log.info(sb.toString());
		}

		// 处理excel相关信息
		excelRows = Excel.parseExcel(excel);
		if (excelRows != null) {
			if (log.isInfoEnabled()) {
				StringBuffer sb = new StringBuffer();
				sb.append("Excel数据:\n");
				for (String[] row : excelRows) {
					for (String cell : row) {
						sb.append(cell).append(", ");
					}
					sb.append("\n");
				}
				log.info(sb.toString());
			}
			if (excelRows.size() > 0) {
				excelHeads = excelRows.get(0);
				excelRows.remove(0);
			}
		}
		// 校验： 验证表头是否重复？是否为系统字段？
		if (excelHeads != null) {
			List<String> listHeads = new ArrayList();
			for (String str : excelHeads) {
				if (listHeads.contains(str)) {
					throw new DemsyException(" Excel数据校验出错：数据表表头不能重复！【" + str + "】列非法");
				}
				if (fields.get(str) == null) {
					throw new DemsyException(" Excel数据校验出错：数据表表头在系统字段中必须存在并在新增时允许编辑！【" + str + "】列非法");
				}
				listHeads.add(str);
			}
			List<String> excelPKs = new ArrayList();
			int len = excelRows.size();
			for (int i = 0; i < len; i++) {
				String[] row = excelRows.get(i);
				if (row == null || row.length <= 1) {
					throw new DemsyException(" Excel数据校验出错：数据表至少需要 2 列！但第 " + (i + 2) + " 行只有 "
							+ (row == null ? 0 : row.length) + " 列");
				}
				if (row[0] == null || row[0].trim().length() == 0) {
					throw new DemsyException(" Excel数据校验出错：数据表的第一列不允许为空！第 " + (i + 2) + " 行第一列非法");
				}
				if (excelPKs.contains(row[0])) {
					throw new DemsyException(" Excel数据校验出错：数据表的第一列不允许重复！第 " + (i + 2) + " 行第一列非法");
				}
				excelPKs.add(row[0]);
			}
		}
	}

	public int save() throws InstantiationException, IllegalAccessException, DemsyException {
		IBizSession session = Demsy.bizSession;
		Long softID = Demsy.me().getSoft().getId();

		IBizField pkfld = fields.get(excelHeads[0]);
		final List<IBizEntity> list = new ArrayList();
		int rowIndex = 1;
		int colIndex = 0;
		for (String[] row : excelRows) {
			rowIndex++;
			IBizEntity data = (IBizEntity) session.load(systemClass, Expr.eq(bizEngine.getPropName(pkfld), row[0]));
			if (data == null) {
				data = (IBizEntity) systemClass.newInstance();
			}
			for (int i = 0; i < row.length; i++) {
				colIndex = i + 1;
				try {
					IBizField fld = fields.get(excelHeads[i]);
					String propname = bizEngine.getPropName(fld);
					String propvalue = row[i];

					IBizSystem fksystem = fld.getRefrenceSystem();
					if (fksystem != null) {
						int dot = propname.indexOf(".");
						if (dot < 0) {
							propname = propname + ".name";
						}
					}

					int dot = propname.indexOf(".");
					// 设置外键值
					if (dot > 0) {
						log.debug(propname);

						String nextprop = propname.substring(dot + 1);
						propname = propname.substring(0, dot);

						if (fksystem == null && fld.getRefrenceField() != null) {
							fksystem = fld.getRefrenceField().getSystem();
						}
						if (fksystem == null) {
							throw new DemsyException("Excel表中的第 " + colIndex + " 列【" + fld.getName()
									+ "】是外键字段，但引用的系统不存在！");
						}
						Object fkvalue = null;
						if (!Str.isEmpty(propvalue)) {
							fkvalue = session.load(bizEngine.getType(fksystem), Expr.eq(nextprop, propvalue));
							if (fkvalue == null) {
								throw new DemsyException("Excel表中的第 " + colIndex + " 列【" + fld.getName() + "】是外键字段，但第 "
										+ rowIndex + " 行【" + propvalue + "】在【" + fksystem.getName() + "】模块中不存在！");
							}
						}
						Obj.setValue(data, propname, fkvalue);
					} else {
						Obj.setValue(data, propname, propvalue);
					}
				} catch (Throwable e) {
					throw new DemsyException("导入Excel中的第 " + rowIndex + " 行第 " + colIndex + " 列时出错！" + e.getMessage());
				}
			}

			//
			data.setSoftID(softID);

			list.add(data);
		}

		try {
			session.save(list);
		} catch (Throwable e) {
			log.error(Ex.msg(e));
			throw new DemsyException(e);
		}
		return excelRows.size();
	}
}
