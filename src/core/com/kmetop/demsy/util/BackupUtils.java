package com.kmetop.demsy.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import com.kmetop.demsy.lang.Dates;
import com.kmetop.demsy.lang.Files;

public class BackupUtils {
	public static String dateFormat = "yyyyMMddHHmm";

	public static void backup(File file, String backupDate, String srcDirName, String destDirName) throws ParseException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				backup(files[i], backupDate, srcDirName, destDirName);
			}
		} else {
			// 拷贝文件
			String path = file.getAbsolutePath();// 原来的文件

			// 删除垃圾文件
			if (path.indexOf(File.separator + "Thumbs.db") > -1) {
				file.delete();
				return;
			}
			
			//Mac 文件夹标识
			if(file.getName().startsWith(".")){
				return;
			}

			// 项目WEB目录标志
			String pathPost = path.substring(srcDirName.length());
			if ((pathPost.startsWith(File.separator + "web" + File.separator) // 是WEB-INF目录
					&& (// 除了WEB-INF的下列子目录外忽略
					!pathPost.startsWith(File.separator + "web" + File.separator + "WEB-INF" + File.separator + "config" + File.separator)//
							&& !pathPost.startsWith(File.separator + "web" + File.separator + "WEB-INF" + File.separator + "resin-web.xml")//
							&& !pathPost.startsWith(File.separator + "web" + File.separator + "WEB-INF" + File.separator + "web.xml")//
					&& !pathPost.startsWith(File.separator + "web" + File.separator + "index.html")//
					)//
					)// end WEB-INF
					|| pathPost.indexOf(File.separator + "CVS" + File.separator) > -1 //
					|| pathPost.indexOf(".cvsignore") > -1 //
					|| pathPost.indexOf(".svn") > -1//
					|| pathPost.startsWith(File.separator + "web" + File.separator + "upload" + File.separator + "patch" + File.separator) //
					|| pathPost.startsWith(File.separator + "web" + File.separator + "WEB-INF" + File.separator + "tmp" + File.separator) //
					|| pathPost.startsWith(File.separator + "scripts" + File.separator) //
					|| pathPost.startsWith(File.separator + "jars" + File.separator) //
					|| pathPost.startsWith(File.separator + "tmp" + File.separator) //
					|| pathPost.startsWith(File.separator + "tomcatlib" + File.separator) //
			) {
				return;
			}

			// 备份相关文件
			String distFilePath = path.replace(srcDirName, destDirName);// 拷贝过去的文件
			long today = Dates.parse(backupDate, dateFormat).getTime();
			long lastModified = file.lastModified();
			if (lastModified > today) {
				File distFile = new File(distFilePath);
				try {
					if (file.exists()) {
						distFile.getParentFile().mkdirs();
						distFile.createNewFile();
						Files.copy(file, distFile);
					}
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}
}
