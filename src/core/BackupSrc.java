import java.io.File;
import java.util.Date;
import java.util.List;

import com.kmetop.demsy.lang.Dates;
import com.kmetop.demsy.lang.Files;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.lang.Zips;
import com.kmetop.demsy.util.BackupUtils;

public class BackupSrc {

	public static String userdir = System.getProperty("user.dir") + File.separator;

	public static String sftName = new File(userdir).getName().toUpperCase();

	public static String backupDate = null;

	public static void main(String[] args) {

		// backupDate = Dates.getCurrentDate(dateFormat);
		// backupDate = backupDate.substring(0, 8) + "0101";
		backupDate = "201204150101";

		String backupDirs = "";
		int count = 0;
		for (String str : args) {
			switch (count) {
			case 0:
				sftName = str;
				break;
			case 1:
				backupDate = args[1];
				break;
			default:
				backupDirs += "," + str;
				break;
			}
			count++;
		}
		if (backupDirs.length() > 0)
			backupDirs = backupDirs.substring(1);
		try {
			String srcDirName = userdir + "src";
			String buildDirName = userdir + "build";
			String destDirName = buildDirName + File.separator + "tmp";
			File backupDir = new File(destDirName);
			Files.deleteDir(backupDir);
			backupDir.mkdirs();
			File srcDir = new File(srcDirName);
			File buildDir = new File(buildDirName);
			File destDir = new File(destDirName + File.separator + "src");
			File destBuildDir = new File(destDirName + File.separator + "src" + File.separator + "build");
			BackupUtils.backup(srcDir, backupDate, new File(srcDirName).getAbsolutePath(), destDir.getAbsolutePath());
			BackupUtils.backup(buildDir, backupDate, new File(buildDirName).getAbsolutePath(), destBuildDir.getAbsolutePath());

			List<String> srcList = Str.toList(backupDirs, ",");
			for (int i = 0; i < srcList.size(); i++) {
				String bkName = srcList.get(i);
				i++;
				String srcDirName1 = srcList.get(i);

				File srcDir1 = new File(srcDirName1);
				BackupUtils.backup(srcDir1, backupDate, srcDirName1, destDirName + File.separator + bkName);
			}

			String backupName = sftName + "-backup(" + Dates.formatDate(new Date(), BackupUtils.dateFormat) + "-" + backupDate + ")";

			String zip = userdir + ".." + File.separator + "temp" + File.separator + backupName + ".zip";
			Zips.zip(destDir.getAbsolutePath(), zip);

			Files.deleteDir(destDir);
			Files.deleteDir(destBuildDir);

			System.out.print(backupName);
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
