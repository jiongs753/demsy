package com.kmetop.demsy.engine;

import static com.kmetop.demsy.Demsy.appconfig;
import static com.kmetop.demsy.Demsy.contextDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.nutz.lang.Files;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.mvc.servlet.StaticResourceFilter;

public abstract class BizCompiler {
	protected static Log log = Logs.getLog(BizCompiler.class);

	public static final String version = "110901-1706";

	private static Pattern var = Pattern.compile("^[_a-zA-Z][_a-zA-Z0-9]*$", Pattern.CASE_INSENSITIVE);

	// 编译次数
	private static int times = 0;

	private static final String TAB = "    ";

	protected BizEngine engine;

	protected BizCompiler(BizEngine engine) {
		this.engine = engine;
	}

	/**
	 * 检查能否可以自动生成系统源代码。
	 * <p>
	 * 空值——表示可以
	 * <p>
	 * 非空——表示不可以
	 * 
	 * @param system
	 *            业务系统
	 * @return 提示信息
	 *         <p>
	 *         空值——表示可以
	 *         <p>
	 *         非空——表示不可以
	 */
	protected String checkSrc(IBizSystem system) {
		if (system.isDisabled())
			return system.getName() + "(" + system.getCode() + "," + system.getId() + ")系统被停用;";
		if (!Str.isEmpty(system.getMappingClass()))
			return system.getName() + "(" + system.getCode() + "," + system.getId() + ")是内置模块;";

		return "";
	}

	/**
	 * 检查能否可以自动生成字段源代码。
	 * <p>
	 * 空值——表示可以
	 * <p>
	 * 非空——表示不可以
	 * 
	 * @param field
	 * @return 提示信息
	 *         <p>
	 *         空值——表示可以
	 *         <p>
	 *         非空——表示不可以
	 */
	protected String checkSrc(IBizSystem system, IBizField field) {
		String prop = engine.getPropName(field);
		String name = field.getName() + "(" + prop + ")";
		if (field.isDisabled())
			return name + "字段被停用;";
		if (field.getRefrenceField() != null)
			return name + "是引用字段;";
		if (field.isTransientField())
			return name + "是临时字段;";
		if (engine.isBuildin(system, prop))
			return name + "是内置字段;";
		if (Str.isEmpty(engine.getClassName(field)))
			return name + "字段类型不存在;";

		return "";
	}

	/**
	 * 生成package语句源代码
	 * 
	 * @return 源代码片段
	 */
	protected String genSrcOfPackage(IBizSystem system) {
		return new StringBuffer().append("package ").append(engine.getPackageOfAutoSystem(system)).append(";")
				.toString();
	}

	/**
	 * 生成import语句源代码
	 * 
	 * @return 源代码片段
	 */
	protected abstract String genSrcOfImport(IBizSystem system);

	/**
	 * 生成类定义源代码
	 * 
	 * @param system
	 *            业务系统
	 */
	protected String genSrcOfClassDeclare(IBizSystem system) {
		StringBuffer src = new StringBuffer();

		String tableName = engine.getTableName(system);
		String className = engine.getSimpleClassName(system);
		String extendClass = engine.getExtendSimpleClassName(system);

		src.append("\n");

		src.append("\n@Entity");
		if (!Str.isEmpty(tableName)) {
			src.append("\n@Table(name = \"" + tableName + "\")");
		}
		src.append("\n@BzSys(")//
				.append("name = \"").append(system.getName().replace("\"", "\\\"")).append("\"")//
				.append(", id = ").append(system.getId())//
				.append(", code = \"").append(system.getCode().replace("\"", "\\\"")).append("\"")//
				.append(", version = \"").append(engine.getVersion(system)).append("\"")//
		;
		src.append(")");
		src.append("\npublic class ").append(className).append(" extends ").append(extendClass).append(" {");

		// TODO: 扩展类特殊属性

		return src.toString();
	}

	/**
	 * 生成引出字段注解
	 * 
	 * @return 源代码片段
	 */
	protected String genSrcOfTargetAnnotation(IBizField fld) {
		StringBuffer src = new StringBuffer();

		String mappedBy = engine.getPropName(fld);

		src.append("\n");
		if (engine.isManyToMany(fld)) {
			src.append("\n").append(TAB).append("@ManyToMany(mappedBy = \"" + mappedBy + "\")");
		} else if (engine.isManyToOne(fld)) {
			src.append("\n").append(TAB).append("@OneToMany(mappedBy = \"" + mappedBy + "\")");
		}
		src.append("\n").append(TAB).append("@BzFld(")//
				.append("name = \"").append(fld.getName().replace("\"", "\\\"")).append("\"")//
				.append(", id = ").append(fld.getId())//
				.append(", code = \"").append(fld.getCode().replace("\"", "\\\"")).append("\"");
		src.append(")");

		return src.toString();
	}

	/**
	 * 生成字段注解
	 * 
	 * @return 源代码片段
	 */
	private String genSrcOfAnnotation(IBizField fld) {
		StringBuffer src = new StringBuffer();

		src.append("\n");

		if (engine.isManyToOne(fld)) {
			src.append("\n").append(TAB).append("@ManyToOne");
		} else if (engine.isNumber(fld)) {
			int p = engine.getPrecision(fld);
			Integer scale = fld.getScale();
			if (scale == null) {
				scale = 0;
			}
			if (p > 0 && p > scale) {
				src.append("\n").append(TAB).append("@Column(precision = ").append(p).append(", scale = ")
						.append(scale).append(")");
			}
		} else if (engine.isManyToMany(fld)) {
			src.append("\n").append(TAB).append("@ManyToMany");
		} else if (engine.isRichText(fld)) {
			src.append("\n").append(TAB).append("@Column(columnDefinition = \"text\")");
		} else if (engine.isUpload(fld)) {
			src.append("\n@Column(length = 256)");
		} else if (engine.isString(fld)) {
			int p = engine.getPrecision(fld);
			if (p > 2000)
				src.append("\n").append(TAB).append("@Column(columnDefinition = \"text\")");
			else
				src.append("\n").append(TAB).append("@Column(length = ").append(p).append(")");
		}

		src.append("\n").append(TAB)
				.append("@BzFld(")
				//
				.append("name = \"").append(fld.getName() == null ? "" : fld.getName().replace("\"", "\\\""))
				.append("\"")
				//
				.append(", id = ").append(fld.getId())
				//
				.append(", code = \"").append(fld.getCode() == null ? "" : fld.getCode().replace("\"", "\\\""))
				.append("\"");
		src.append(")");

		return src.toString();
	}

	/**
	 * 生成属性定义源代码
	 * 
	 * @param propType
	 *            属性类型
	 * @param propName
	 *            属性名称
	 * @return 源代码片段
	 */
	private String genSrcOfPropDeclare(String propType, String propName, String comment) {
		StringBuffer src = new StringBuffer();

		src.append("\n").append(TAB).append("private " + propType + " " + propName + ";");
		src.append(" // ").append(comment);

		return src.toString();
	}

	/**
	 * 生成属性Getter/Setter源代码
	 * 
	 * @param propType
	 *            属性类型
	 * @param propName
	 *            属性名称
	 * @return 源码片段
	 */
	private String genSrcOfPropGetterSetter(String propType, String propName, String comment) {
		StringBuffer src = new StringBuffer();

		String upperName = propName.substring(0, 1).toUpperCase() + propName.substring(1);

		src.append("\n");

		src.append("\n").append(TAB).append("public void set" + upperName + "(" + propType + " param) {");
		src.append("\n").append(TAB).append(TAB).append(propName + " = param;");
		src.append("\n").append(TAB).append("}");

		src.append("\n");

		src.append("\n").append(TAB).append("public " + propType + " get" + upperName + "() {");
		src.append("\n").append(TAB).append(TAB).append("return " + propName + ";");
		src.append("\n").append(TAB).append("}");

		return src.toString();
	}

	/**
	 * toString() method src code
	 * 
	 * @param propType
	 * @param propName
	 * @return
	 */
	private String genSrcOfToString(String propType, String propName) {
		StringBuffer src = new StringBuffer();

		src.append("\n");

		src.append("\n").append(TAB).append("public String toString() {");
		if ("String".equals(propType))
			src.append("\n").append(TAB).append(TAB).append("return ").append(propName).append(";");
		else
			src.append("\n").append(TAB).append(TAB).append("return ").append(propName).append("==null?\"\":")
					.append(propName).append(".toString();");
		src.append("\n").append(TAB).append("}");

		return src.toString();
	}

	private void appendSrcInfo(List<String> flds, StringBuffer msg, String name) {
		if (flds != null && flds.size() > 0) {
			msg.append(name).append("[");
			for (String str : flds) {
				msg.append(str).append(",");
			}
			msg.append("]");
		}
	}

	protected String genSrcOfSystem(StringBuffer src, IBizSystem system) {
		String info = checkSrc(system);
		if (!Str.isEmpty(info)) {
			return info;
		}

		StringBuffer msg = new StringBuffer();

		src.append(genSrcOfPackage(system));
		src.append(genSrcOfImport(system));
		src.append(genSrcOfClassDeclare(system));

		// 检查字段
		List<IBizField> checkedFields = new LinkedList();
		List<? extends IBizField> fields = engine.getFieldsOfEnabled(system);
		if (fields != null && fields.size() > 0) {
			List<String> disFlds = new LinkedList();
			List<String> refFlds = new LinkedList();
			List<String> refDisSysFlds = new LinkedList();
			List<String> tmpFlds = new LinkedList();
			List<String> bdnFlds = new LinkedList();
			List<String> empTypeFlds = new LinkedList();
			List<String> otherFlds = new LinkedList();
			List<String> formalFields = new LinkedList();
			for (IBizField fld : fields) {
				String prop = engine.getPropName(fld);

				info = fld.getName() + "(" + fld.getPropName() + "," + fld.getId() + ")";
				if (Str.isEmpty(prop) || !var.matcher(prop).find())
					otherFlds.add(fld.getCode());
				else if (fld.isDisabled())
					disFlds.add(info);
				else if (fld.getRefrenceField() != null || prop.indexOf(".") > -1)
					refFlds.add(info);
				else if (fld.getRefrenceSystem() != null && fld.getRefrenceSystem().isDisabled())
					refDisSysFlds.add(info);
				else if (fld.isTransientField())
					tmpFlds.add(info);
				else if (engine.isBuildin(system, prop))
					bdnFlds.add(info);
				else if (!var.matcher(engine.getClassName(fld)).find())
					empTypeFlds.add(info);
				else {
					formalFields.add(info);
					checkedFields.add(fld);
				}
			}
			appendSrcInfo(formalFields, msg, "\n\t源码字段");
			appendSrcInfo(disFlds, msg, "\n\t停用字段");
			appendSrcInfo(refDisSysFlds, msg, "\n\t引用系统被停用");
			appendSrcInfo(refFlds, msg, "\n\t引用字段");
			appendSrcInfo(tmpFlds, msg, "\n\t临时字段");
			appendSrcInfo(bdnFlds, msg, "\n\t内置字段");
			appendSrcInfo(empTypeFlds, msg, "\n\t空类型字段");
			appendSrcInfo(otherFlds, msg, "\n\t其他非法字段");
		}

		// 检查引出字段
		// List<IBizField> checkedExportFields = new LinkedList();
		// fields = this.getExportFields(system);
		// if (fields != null && fields.size() > 0) {
		// for (IBizField fld : fields) {
		// info = checkSrc(system, fld);
		// if (!Strings.isEmpty(info)) {
		// IBizSystem exportSys = fld.getSystem();
		// msg.append(exportSys.getName()).append("(" + exportSys.getCode() +
		// ")").append("::").append(info);
		// } else {
		// checkedExportFields.add(fld);
		// }
		// }
		// }

		// 生成属性声明语句
		for (IBizField fld : checkedFields) {
			src.append(genSrcOfAnnotation(fld));
			src.append(genSrcOfPropDeclare(engine.getClassName(fld), engine.getPropName(fld), fld.getName()));
		}
		// for (IBizField fld : checkedExportFields) {
		// src.append(genSrcOfTargetAnnotation(fld));
		// src.append(genSrcOfPropDeclare(getTargetClassName(fld),
		// getTargetPropName(fld), fld.getName()));
		// }

		// 生成属性 GETTER/SETTER F语句
		for (IBizField fld : checkedFields) {
			src.append(genSrcOfPropGetterSetter(engine.getClassName(fld), engine.getPropName(fld), fld.getName()));
		}
		// for (IBizField exportField : checkedExportFields) {
		// String type = "List<" + getClassSimpleName(exportField.getSystem()) +
		// ">";
		// String prop = getTargetPropName(exportField);
		//
		// src.append(genSrcOfPropGetterSetter(type, prop,
		// exportField.getName()));
		// }
		List<? extends IBizField> gridFields = engine.getFieldsOfGrid(system, null);
		if (gridFields.size() > 0) {
			String propType = null;
			String propName = null;
			for (IBizField fld : gridFields) {
				if (engine.isString(fld)) {
					propType = "String";
					propName = engine.getPropName(fld);
					if (propName.indexOf(".") < 0)
						break;
				}
			}
			if (propName != null)
				src.append(this.genSrcOfToString(propType, propName));
		}

		// 类声明语句结束
		src.append("\n}");

		return msg.toString();
	}

	/**
	 * 编译业务系统，现将业务系统类编译在临时目录下。
	 * <p>
	 * 编译业务系统时，将检查该系统所依赖的其他业务系统类是否存在，如不存在，则将同时编译依赖系统。
	 * 
	 * @param system
	 *            业务系统
	 * @param copyToClassesDir
	 *            编译成功后是否拷贝到/WEB-INF/classes目录下
	 * @return 编译业务系统后返回信息列表
	 * @throws DemsyException
	 *             编译业务实体类出错将抛出编译错误异常
	 */
	public synchronized List<String> compileSystem(IBizSystem system, boolean copyToClassesDir) throws DemsyException {
		if (log.isInfoEnabled()) {
			log.infof("编译<%s(%s,%s)>业务系统......", system.getName(), system.getCode(), system.getId());
		}

		List<IBizSystem> collectedSystems = new LinkedList();
		collectedSystems.add(system);

		if (log.isInfoEnabled()) {
			log.infof("编译<%s(%s,%s)>业务系统: 计算需要编译的依赖系统...", system.getName(), system.getCode(), system.getId());
		}

		collectBizSystems(collectedSystems, system);

		if (log.isInfoEnabled()) {
			StringBuffer logBuf = new StringBuffer();
			for (IBizSystem sys : collectedSystems) {
				logBuf.append(sys).append("(" + sys.getCode() + "," + sys.getId() + ")")
						.append(engine.getSimpleClassName(sys)).append("\n");
			}
			log.infof("编译<%s(%s,%s)>业务系统: 计算需要编译的依赖系统: 结束. 共<%s>个系统需编译[\n%s]", system.getName(), system.getCode(),
					system.getId(), collectedSystems.size(), logBuf);
		}

		String tempDir = appconfig.getTempDir() + File.separator + "BIZSRC_" + (times++);
		Files.deleteDir(new File(tempDir));

		List<String> classNames = new LinkedList();
		List<String> srcCodes = new LinkedList();
		List<String> infos = new LinkedList();

		String error = null;
		try {
			// 过滤已编译过的依赖类
			for (int i = collectedSystems.size() - 1; i >= 0; i--) {
				IBizSystem sys = collectedSystems.get(i);
				boolean igloreSrc = false;
				if (!system.equals(sys)) {
					try {
						Cls.forName(engine.getClassName(sys));
						igloreSrc = true;
					} catch (ClassNotFoundException e) {
					}
				}
				if (igloreSrc) {
					collectedSystems.remove(i);
				}
			}

			// 生成源代码
			String className;
			StringBuffer srcCode;
			String info;
			for (IBizSystem sys : collectedSystems) {
				className = engine.getClassName(sys);
				srcCode = new StringBuffer();
				info = genSrcOfSystem(srcCode, sys);

				if (info.length() > 0)
					infos.add(sys.getName() + "(" + sys.getCode() + "," + sys.getId() + ")：" + info);

				if (srcCode.length() > 0) {
					classNames.add(className);
					srcCodes.add(srcCode.toString());
					genSrcFile(tempDir, className, srcCode.toString());
				}
			}

			if (srcCodes.size() > 0)
				error = compileSrcFile(tempDir, classNames, srcCodes);

		} catch (IOException e) {
			throw new DemsyException(e);
		}

		if (!Str.isEmpty(error)) {
			// log.errorf("编译<%s(%s,%s)>业务系统出错: %s", system.getName(),
			// system.getCode(), system.getId(), error);
			throw new DemsyException(error);
		} else {
			if (log.isInfoEnabled())
				log.infof("编译<%s(%s,%s)>业务系统: 结束.", system.getName(), system.getCode(), system.getId());

			if (copyToClassesDir)
				try {
					Files.copyDir(new File(tempDir), new File(appconfig.getClassDir()));

					Files.deleteDir(new File(tempDir));
				} catch (IOException e) {
					throw new DemsyException(e);
				}
		}

		return infos;
	}

	/**
	 * 收集需要编译的业务系统
	 * 
	 * @param collectedSystems
	 *            编译目标业务系统时同时需要依赖编译的系统
	 * @param targetSystem
	 *            编译的目标业务系统
	 */
	private void collectBizSystems(List collectedSystems, IBizSystem targetSystem) {
		if (!collectedSystems.contains(targetSystem)) {
			collectedSystems.add(targetSystem);
		}
		List<? extends IBizField> fields = engine.getFieldsOfSystemFK(targetSystem);
		for (IBizField fld : fields) {
			if (!fld.isDisabled()) {
				IBizSystem refSys = fld.getRefrenceSystem();
				if (!collectedSystems.contains(refSys) && !refSys.isDisabled()) {
					if (log.isDebugEnabled())
						log.debugf("系统<%s(%s,%s)>字段<%s(%s,%s)>引用到系统<%s(%s,%s)>", targetSystem.getName(),
								targetSystem.getCode(), targetSystem.getId(), fld.getName(), fld.getCode(),
								fld.getId(), refSys.getName(), refSys.getCode(), refSys.getId());

					collectBizSystems(collectedSystems, refSys);
				}
			}
		}
		// fields = getExportFields(system);
		// for (IBizField fld : fields) {
		// if (!fld.isDisabled()) {
		// IBizSystem refSys = fld.getSystem();
		// if (log.isDebugEnabled())
		// log.debugf("系统<%s(%s)>被系统<%s(%s)字段<%s(%s)>引用>", system.getName(),
		// system.getCode(), refSys.getName(), refSys.getCode(), fld.getName(),
		// fld.getCode());
		// if (!systems.contains(refSys))
		// buildBizSystems(systems, refSys);
		// }
		// }
	}

	/**
	 * 编译源文件并将编译成功后的类文件拷贝到/WEB-INF/classes目录同时删除临时目录
	 * 
	 * @param tempDir
	 *            编译时使用的临时目录
	 * @param classNames
	 *            待编译的类列表
	 * @param srcCodes
	 *            源代码列表
	 * @return 编译成功返回 null， 编译出错返回错误信息。
	 * @throws IOException
	 */
	private String compileSrcFile(String tempDir, List<String> classNames, List<String> srcCodes) throws IOException {
		List<String> classpath = new LinkedList();

		// java 类路径
		StringTokenizer token = new StringTokenizer(System.getProperty("java.class.path") + ";"
				+ System.getProperty("sun.boot.class.path"), ";");
		while (token.hasMoreElements()) {
			String path = token.nextToken();
			File jar = new File(path);
			if (jar.exists() && (jar.isFile() && (path.endsWith(".jar") || path.endsWith(".zip")))) {
				path = jar.getAbsolutePath();
				if (!classpath.contains(path))
					classpath.add(path);
			}
		}

		String webinfo = contextDir + File.separator + "WEB-INF" + File.separator;

		// WEB-INF/classes 路径
		String path = webinfo + "classes" + File.separator;
		path = new File(path).getAbsolutePath();
		if (!classpath.contains(path))
			classpath.add(path);
		if (!Demsy.appconfig.isProductMode())
			classpath.add(StaticResourceFilter.classPathForDir);

		// WEB-INF/lib/*.jar zip 路径
		File libDir = new File(webinfo + "lib" + File.separator);
		if (libDir.exists() && libDir.isDirectory()) {
			for (File jar : libDir.listFiles()) {
				path = jar.getAbsolutePath();
				if (jar.exists() && jar.isFile() && (path.endsWith(".jar") || path.endsWith(".zip"))) {
					if (!classpath.contains(path))
						classpath.add(path);
				}
			}
		}

		// 系统编译单元
		SystemCompilationUnit[] units = new SystemCompilationUnit[classNames.size()];
		int unitCount = 0;
		File javaFile = null;
		for (String className : classNames) {
			String fileName = tempDir + File.separator + className.replace(".", File.separator);
			String srcCode = srcCodes.get(unitCount);
			javaFile = new File(fileName + ".java");

			units[unitCount] = new SystemCompilationUnit(className.substring(className.lastIndexOf(".") + 1), srcCode,
					javaFile, null);

			unitCount++;
		}

		File pkgDir = javaFile.getParentFile();

		String error = compileUnits(units, classpath, pkgDir);
		if (!Str.isEmpty(error)) {
			return error;
		}

		for (SystemCompilationUnit unit : units) {
			OutputStream os = null;
			try {
				File classFile = new File(pkgDir.getAbsolutePath() + File.separator + unit.getName() + ".class");

				if (!classFile.exists()) {
					if (!classFile.getParentFile().exists()) {
						classFile.getParentFile().mkdirs();
					}
					classFile.createNewFile();
				}

				os = new FileOutputStream(classFile);
				os.write((byte[]) unit.getCompileData());

				os.flush();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
					}
				}
			}
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	private String compileUnits(SystemCompilationUnit[] units, List<String> jars, File tempDirFile) {
		StringBuffer problemBuffer = new StringBuffer();

		StringBuffer logPaths = new StringBuffer("CLASSPATH：");
		String[] classpath = new String[jars.size()];
		int count = 0;
		for (String jar : jars) {
			logPaths.append("\n").append(jar);
			classpath[count++] = jar;
		}
		log.debug(logPaths);

		ICompilationUnit[] compilationUnits = new ICompilationUnit[units.length];
		for (int i = 0; i < compilationUnits.length; i++) {
			compilationUnits[i] = new CompilationUnit(units[i].getSrcCode().toCharArray(), units[i].getSourceFile()
					.getAbsolutePath(), "UTF-8");
		}

		count = 0;
		String[] javafiles = new String[units.length];
		for (SystemCompilationUnit unit : units) {
			javafiles[count++] = unit.getSourceFile().getAbsolutePath();
		}

		INameEnvironment env = new FileSystem(classpath, javafiles, null);// getNameEnvironment(units);

		IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.proceedWithAllProblems();

		Map settings = getJdtSettings();

		IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());

		ICompilerRequestor requestor = getCompilerRequestor(units, problemBuffer);

		Compiler compiler = new Compiler(env, policy, settings, requestor, problemFactory);
		compiler.compile(compilationUnits);

		if (problemBuffer.length() > 0) {
			return problemBuffer.toString();
		}

		return null;
	}

	private ICompilerRequestor getCompilerRequestor(final SystemCompilationUnit[] units,
			final StringBuffer problemBuffer) {
		return new ICompilerRequestor() {
			public void acceptResult(CompilationResult result) {
				String className = new String(((CompilationUnit) result.getCompilationUnit()).mainTypeName);

				int classIdx;
				for (classIdx = 0; classIdx < units.length; ++classIdx) {
					if (className.equals(units[classIdx].getName())) {
						break;
					}
				}

				if (result.hasErrors()) {
					String sourceCode = units[classIdx].getSrcCode();

					IProblem[] problems = result.getErrors();
					for (int i = 0; i < problems.length; i++) {
						IProblem problem = problems[i];
						problemBuffer.append(i + 1);
						problemBuffer.append(". ");
						problemBuffer.append(problem.getMessage());

						if (problem.getSourceStart() >= 0 && problem.getSourceEnd() >= 0) {
							int problemStartIndex = sourceCode.lastIndexOf("\n", problem.getSourceStart()) + 1;
							int problemEndIndex = sourceCode.indexOf("\n", problem.getSourceEnd());
							if (problemEndIndex < 0) {
								problemEndIndex = sourceCode.length();
							}

							problemBuffer.append("\n");
							problemBuffer.append(sourceCode.substring(problemStartIndex, problemEndIndex));
							problemBuffer.append("\n");
							for (int j = problemStartIndex; j < problem.getSourceStart(); j++) {
								problemBuffer.append(" ");
							}
							if (problem.getSourceStart() == problem.getSourceEnd()) {
								problemBuffer.append("^");
							} else {
								problemBuffer.append("<");
								for (int j = problem.getSourceStart() + 1; j < problem.getSourceEnd(); j++) {
									problemBuffer.append("-");
								}
								problemBuffer.append(">");
							}
						}

						problemBuffer.append("\n");
					}
					problemBuffer.append(problems.length);
					problemBuffer.append(" errors\n");
				}
				if (problemBuffer.length() == 0) {
					ClassFile[] resultClassFiles = result.getClassFiles();
					for (int i = 0; i < resultClassFiles.length; i++) {
						units[classIdx].setCompileData(resultClassFiles[i].getBytes());
					}
				}
			}
		};
	}

	private Map getJdtSettings() {
		Map settings = new HashMap();
		settings.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.IGNORE);
		settings.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
		settings.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
		settings.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);

		Properties systemProps = System.getProperties();
		for (Enumeration it = systemProps.propertyNames(); it.hasMoreElements();) {
			String propName = (String) it.nextElement();
			if (propName.startsWith("org.eclipse.jdt.core.")) {
				String propVal = systemProps.getProperty(propName);
				if (propVal != null && propVal.length() > 0) {
					settings.put(propName, propVal);
				}
			}
		}

		return settings;
	}

	private void genSrcFile(String tempDir, String className, String srcCode) throws IOException {
		File javaFile = new File(tempDir + File.separator + className.replace(".", File.separator) + ".java");
		if (!javaFile.exists()) {
			javaFile.getParentFile().mkdirs();
			javaFile.createNewFile();
		}
		Reader reader = null;
		Writer writer = null;
		try {
			reader = new StringReader(srcCode);
			OutputStream os = new FileOutputStream(javaFile);
			writer = new OutputStreamWriter(os, "UTF-8");
			int bytesRead = 0;
			char[] buffer = new char[8192];
			while ((bytesRead = reader.read(buffer, 0, 8192)) != -1) {
				writer.write(buffer, 0, bytesRead);
			}
			writer.flush();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}

			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
				}

		}

	}

	@SuppressWarnings("unused")
	private static class SystemCompilationUnit {
		private final String name;

		private final String source;

		private final File sourceFile;

		private final List expressions;

		private Serializable compileData;

		public SystemCompilationUnit(String name, String sourceCode, File sourceFile, List expressions) {
			this.name = name;
			this.source = sourceCode;
			this.sourceFile = sourceFile;
			this.expressions = expressions;
		}

		public String getName() {
			return name;
		}

		public String getSrcCode() {
			return source;
		}

		public File getSourceFile() {
			return sourceFile;
		}

		public List getExpressions() {
			return expressions;
		}

		public void setCompileData(Serializable compileData) {
			this.compileData = compileData;
		}

		public Serializable getCompileData() {
			return compileData;
		}
	}
}
