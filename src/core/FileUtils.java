import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class FileUtils {
	public static void main(String[] args) {
		// noneDeleteCVSFiles("E:\\DEMSY");
		// deleteEmptyFiles("J:\\CVSNT_SERVER\\DEMSY");
		// noneDeleteCVSFiles("E:\\DEMSY");
		deleteCVSFiles(System.getProperty("user.dir"));
	}

	private static void deleteEmptyFiles(String dir) {
		deleteEmptyFiles(new File(dir), new File(dir));
	}

	private static void deleteEmptyCVSDirs(String dir) {
		List<String> onlyContainFiles = new ArrayList();
		onlyContainFiles.add("CVS");
		deleteEmptyDirs(new File(dir), new File(dir), onlyContainFiles);
	}

	private static void noneDeleteCVSFiles(String dir) {
		List<String> nonDeleteFileNames = new ArrayList();
		nonDeleteFileNames.add("CVS");
		deleteOtherFiles(new File(dir), nonDeleteFileNames);
	}

	private static void deleteCVSFiles(String dir) {
		List<String> deletingFileNames = new ArrayList();
		deletingFileNames.add(".cvsignore");
		deletingFileNames.add("CVS");
		deletingFileNames.add(".svn");
		deletingFileNames.add("目录文件已同步");
		deletingFileNames.add("Thumbs.db");
		deleteFiles(new File(dir), deletingFileNames);
	}

	public static void deleteEmptyFiles(File root, File dir) {
		if (dir.isDirectory()) {
			File[] subFiles = dir.listFiles();
			if (subFiles != null) {
				if (subFiles.length == 0) {
					System.out.println("delete folder: " + dir.getAbsolutePath());
					dir.delete();
					if (root.getAbsolutePath().equals(dir.getAbsolutePath()) || root.getAbsolutePath().equals(dir.getParentFile().getAbsolutePath())) {
						return;
					} else {
						deleteEmptyFiles(root, dir.getParentFile());
					}
				}
				for (File subFile : subFiles) {
					deleteEmptyFiles(root, subFile);
				}
			}
		} else {
			if (dir.length() == 0) {
				System.out.println("delete file: " + dir.getAbsolutePath());
				dir.delete();
			}
		}
	}

	/**
	 * 删除只包含指定文件的目录
	 * 
	 * @param dir
	 * @param onlyContainFiles
	 */
	public static void deleteEmptyDirs(File root, File dir, List<String> onlyContainFiles) {
		if (dir.isFile()) {
			return;
		}
		File[] subFiles = dir.listFiles();
		if (subFiles != null && subFiles.length == 1) {
			File oneFile = subFiles[0];
			if (onlyContainFiles.contains(oneFile.getName())) {
				deleteAll(dir);// 删除空目录
				if (root.getAbsolutePath().equals(dir.getAbsolutePath()) || root.getAbsolutePath().equals(dir.getParentFile().getAbsolutePath())) {
					return;
				} else {
					deleteEmptyDirs(root, dir.getParentFile(), onlyContainFiles);
				}
			}
		}
	}

	/**
	 * 删除目录中的指定文件
	 * 
	 * @param dir
	 * @param deleteFiles
	 */
	public static void deleteFiles(File dir, List<String> deleteFiles) {
		String fileName = dir.getName();
		if (deleteFiles.contains(fileName)) {
			deleteAll(dir);
		} else if (dir.isDirectory()) {
			File[] subFiles = dir.listFiles();
			if (subFiles != null) {
				for (File subFile : subFiles) {
					deleteFiles(subFile, deleteFiles);
				}
			}
		}
	}

	/**
	 * 删除目录中除了指定文件以外的其他文件
	 * 
	 * @param dir
	 * @param noneDeleteFiles
	 */
	public static void deleteOtherFiles(File dir, List<String> noneDeleteFiles) {
		String fileName = dir.getName();
		if (dir.isDirectory()) {
			if (noneDeleteFiles.contains(fileName)) {
				return;
			}
			File[] subs = dir.listFiles();
			if (subs != null) {
				for (File sub : subs) {
					deleteOtherFiles(sub, noneDeleteFiles);
				}
			}
		} else {
			System.out.println("delete file: " + dir.getAbsolutePath());
			dir.delete();
		}
	}

	public static void deleteAll(String fileName) {
		deleteAll(new File(fileName));
	}

	public static void deleteAll(File file) {
		deleteAll(file, false);
	}

	public static void deleteAll(File file, boolean deleteEmptyDir) {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteAll(files[i]);
			}
		}
		File parent = file.getParentFile();
		if (file.exists()) {
			System.out.println("delete file: " + file.getAbsolutePath());
			file.delete();
		}
		if (deleteEmptyDir) {
			String[] list = parent.list();
			if (list == null || list.length == 0) {
				deleteAll(parent, true);
			}
		}
	}

}
