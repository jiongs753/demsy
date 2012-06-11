package com.kmetop.demsy.lang;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public abstract class Zips {

	public static final int BUFFER_SIZE = 64 * 1024;

	public static final int UNKNOWN = -1;

	public static final int ZIP = 0;

	public static final int GZIP = 1;

	public static final int TZ = 2;

	public static final int TGZ = 3;

	public static final int TAR = 4;

	public static final int JAR = 5;

	public static final int TZIP = 6;

	private Zips() {
	}

	/**
	 * Unzip a zip file into a directory. At present, can unzip file types:
	 * ".zip", ".jar", ".gzip", ".gz", ".tar.gz", ".tgz", ".tar", ".tar.zip".
	 * 
	 * @param zipFilename
	 *            the zip filename.
	 * @param targetDir
	 *            the target dir which extracted files will be saved in.
	 * @param isRestorePath
	 *            whether restore files with path.
	 * @exception IOException
	 *                if a IOException occurs.
	 * @exception FileNotFoundException
	 *                if a FileNotFoundException occurs.
	 */
	public static void unzip(String zipFilename, String targetDir, boolean isRestorePath) throws IOException, FileNotFoundException {
		int type = getType(zipFilename);
		switch (type) {
		case TGZ:
		case TZ:
			// case TAR:
			// unzipTars(type, zipFilename, targetDir, isRestorePath);
			// break;
		case GZIP:
			unzipGZIP(zipFilename, targetDir, isRestorePath);
			break;
		default:
			unzipZIP(zipFilename, targetDir, isRestorePath);
		}
	}

	/**
	 * Unzip a zip input stream into a directory.
	 * 
	 * @param zin
	 *            the ZipInputStream.
	 * @param targetDir
	 *            the target dir which extracted files will be saved in.
	 * @param isRestorePath
	 *            whether restore files with path.
	 * @exception IOException
	 *                if a IOException occurs.
	 * @exception FileNotFoundException
	 *                if a FileNotFoundException occurs.
	 */
	public static void unzipZIP(ZipInputStream zin, String targetDir, boolean isRestorePath) throws IOException, FileNotFoundException {
		try {
			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				if (!ze.isDirectory()) {
					String fileName = getFilename(ze.getName(), targetDir, isRestorePath);
					saveFile(zin, fileName);
					new File(fileName).setLastModified(ze.getTime());
				} else if (isRestorePath) {
					checkDir(ze.getName(), targetDir);
				}
			}
		} finally {
			zin.close();
		}
	}

	private static void unzipZIP(String zipFilename, String targetDir, boolean isRestorePath) throws IOException, FileNotFoundException {
		unzipZIP(new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFilename))), targetDir, isRestorePath);
	}

	/**
	 * Unzip type of ".tar.gz", ".tgz" and ".tar" file into a directory.
	 * 
	 * @param archiveType
	 *            the archive type.
	 * @param zipFilename
	 *            the zip filename.
	 * @param targetDir
	 *            the target dir which extracted files will be saved in.
	 * @param isRestorePath
	 *            whether restore files with path.
	 * @exception IOException
	 *                if a IOException occurs.
	 * @exception FileNotFoundException
	 *                if a FileNotFoundException occurs.
	 */
	// private static void unzipTars(int archiveType, String zipFilename,
	// String targetDir, boolean isRestorePath) throws IOException,
	// FileNotFoundException {
	// TarInputStream tin = null;
	// try {
	// tin = openTarInputStream(archiveType, zipFilename);
	// TarEntry te = null;
	// while ((te = tin.getNextEntry()) != null) {
	// if (!te.isDirectory()) {
	// saveFile(tin, getFilename(te.getName(), targetDir,
	// isRestorePath));
	// } else if (isRestorePath) {
	// checkDir(te.getName(), targetDir);
	// }
	// }
	// } finally {
	// if (tin != null) {
	// tin.close();
	// tin = null;
	// }
	// }
	// }
	/**
	 * Unzip a gzip file into a directory.
	 * 
	 * @param zipFilename
	 *            the gzip filename.
	 * @param targetDir
	 *            the target dir which extracted files will be saved in.
	 * @param isRestorePath
	 *            whether restore files with path.
	 * @exception IOException
	 *                if a IOException occurs.
	 * @exception FileNotFoundException
	 *                if a FileNotFoundException occurs.
	 */
	private static void unzipGZIP(String zipFilename, String targetDir, boolean isRestorePath) throws IOException, FileNotFoundException {
		InputStream in = null;
		try {
			in = new GZIPInputStream(new FileInputStream(zipFilename));

			String s = new File(zipFilename).getName().toLowerCase();
			int ndx = s.lastIndexOf('.');
			saveFile(in, getFilename(s.substring(0, ndx), targetDir, isRestorePath));
		} finally {
			if (in != null) {
				in.close();
				in = null;
			}
		}
	}

	public static int getType(String archiveFilename) {
		int ret = UNKNOWN;
		String s = archiveFilename.trim().toLowerCase();
		if (s.endsWith(".tar.gz") || s.endsWith(".tgz")) {
			ret = TGZ;
		} else if (s.endsWith(".tar.zip")) {
			ret = TZIP;
			// } else if (s.endsWith(".tar.z")) {
			// ret = TZ;
		} else if (s.endsWith(".tar")) {
			ret = TAR;
		} else if (s.endsWith(".gz") || s.endsWith(".gzip")) {
			ret = GZIP;
		} else if (s.endsWith(".zip")) {
			ret = ZIP;
		} else if (s.endsWith(".jar")) {
			ret = JAR;
		}
		return ret;
	}

	// static private TarInputStream openTarInputStream(int archiveType,
	// String archiveFile) throws IOException {
	//
	// InputStream in = null;
	// switch (archiveType) {
	// case TGZ:
	// // This is a GZIP archive.
	// //
	// // Reopen the archive as a GZIPInputStream.
	// //
	// in = new GZIPInputStream(new FileInputStream(archiveFile));
	// break;
	// case TZIP:
	// // This is a ZIP archive.
	// //
	// // Reopen the archive as a ZIPInputStream, and
	// // position to the first entry. We only handle
	// // the case where the tar archive is the first
	// // entry in the ZIP archive.
	// //
	// ZipInputStream zin = new ZipInputStream(new FileInputStream(
	// archiveFile));
	//
	// // REVIEW
	// // UNDONE
	// // Could/should we use this ZipEntry to set the name
	// // displayed for the root entry of the tree?
	// //
	// ZipEntry zipEnt = zin.getNextEntry();
	//
	// in = zin;
	// break;
	// case TAR:
	// default:
	// // We do not know what this is!
	// //
	// // So, we will assume it is a simple tar archive
	// // and reopen the file as a FileInputStream.
	// //
	// in = new FileInputStream(archiveFile);
	// }
	// return new TarInputStream(in);
	// }

	private static String getFilename(String orgFilename, String targetDir, boolean isRestorePath) {
		String filename = null;
		if (isRestorePath) {
			filename = orgFilename;
			if (filename.startsWith("/") || filename.startsWith("\\")) {
				filename = filename.substring(1);
			}
			filename = targetDir + filename;
			String p = new File(filename).getParent();
			if (p != null && p.length() > 0) {
				File pf = new File(p);
				if (!pf.exists()) {
					pf.mkdirs(); // ?
				}
			}
		} else {
			filename = targetDir + new File(orgFilename).getName();
		}
		return filename;
	}

	private static void checkDir(String dirName, String targetDir) {
		String filename = dirName;
		if (filename.startsWith("/") || filename.startsWith("\\")) {
			filename = filename.substring(1);
		}
		filename = targetDir + filename;
		File pf = new File(filename);
		if (!pf.exists()) {
			pf.mkdirs(); // ?
		}
	}

	private static void saveFile(InputStream in, String filename) throws IOException, FileNotFoundException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(filename));
			byte[] buf = new byte[BUFFER_SIZE];
			int len;
			while ((len = in.read(buf)) >= 0) {
				out.write(buf, 0, len);
			}
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static void zip(String folder, String zipfile) throws IOException {
		ZipArchive zip = new ZipArchive();
		zip.open(zipfile, "w");
		zip.compress(folder, "", true);
		zip.close();
	}

	private static final class ZipArchive {

		public final static String MODE_WRITE = "w";

		public final static String MODE_READ = "r";

		private ZipOutputStream zout;

		private ZipFile zin;

		private ArrayList list = new ArrayList();

		private StringBuffer buf = new StringBuffer();

		private byte[] dbf = new byte[2048];

		public ZipArchive() {
			super();
		}

		/**
		 * Open a zip file with mode "w" <code>ZipArchive.MODE_WRITE</code> or
		 * mode "r" <code>ZipArchive.MODE_READ</code>.
		 * <p>
		 * When with mode "w", you can compress a file or a folder to zip entry.
		 * <p>
		 * When with mode "r", you can expand a zip entry to a folder.
		 * 
		 * @param zipName
		 * @param mode
		 * @return
		 * 
		 * @throws IOException
		 */
		public boolean open(String zipName, String mode) throws IOException {
			String m = mode.toLowerCase();

			if (MODE_WRITE.equals(m)) {
				File f = new File(zipName);
				if (!f.exists()) {
					File pf = f.getParentFile();
					if (pf != null) {
						pf.mkdirs();
					}
				}
				zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipName)));
			} else if (MODE_READ.equals(m)) {
				zin = new ZipFile(zipName);
				Enumeration en = zin.entries();
				while (en.hasMoreElements()) {
					ZipEntry ze = (ZipEntry) en.nextElement();
					list.add(ze.getName());
				}
			}

			return true;
		}

		/**
		 * Close the <code>ZipArchive</code>
		 * <p>
		 * An opened <code>ZipArchive</code> must be closed after operating.
		 * 
		 * @throws IOException
		 */
		public void close() throws IOException {
			if (zout != null) {
				zout.close();
				zout = null;
			}
			if (zin != null) {
				zin.close();
				zin = null;
			}
		}

		/**
		 * Compress a file or a folder to a zip entry.
		 * 
		 * @param path
		 *            the file path that you wnat ot compress to a zip entry.
		 * @param entryName
		 *            the zip entry taht you want to compress something into it.
		 * @param isSelfContains
		 *            a flag that determine whether include self.
		 * @return bytes of write to zip archive.
		 * 
		 * @throws IOException
		 */
		public int compress(String path, String entryName, boolean isSelfContains) throws IOException {
			if (zout == null) {
				throw new IOException("The zip file has not been opened.");
			}

			if (entryName == null || entryName.trim().length() == 0 || entryName.equals("/")) {
				return compress0(new File(path), "", isSelfContains);
			} else {
				return compress0(new File(path), entryName, isSelfContains);
			}
		}

		/**
		 * Expand a zip entry to a folder.
		 * 
		 * @param entryName
		 *            the zip entry that you want to expand.
		 * @param folder
		 *            the folder that you want store the expanded zip entry.
		 * @param isSelfContains
		 *            a flag that determine whether include self.
		 * @return bytes that expand from zip archive.
		 * 
		 * @throws IOException
		 */
		public int expand(String entryName, String folder, boolean isSelfContains) throws IOException {
			if (zin == null) {
				throw new IOException("The zip file has not been opened.");
			}

			if (entryName == null || entryName.trim().length() == 0 || entryName.equals("/")) {
				int ret = 0;
				Iterator it = list.iterator();
				while (it.hasNext()) {
					ret += expand0(zin.getEntry((String) it.next()), folder, true);
					it = list.iterator();
				}
				return ret;
			} else {
				return expand0(zin.getEntry(entryName), folder, isSelfContains);
			}
		}

		private int compress0(File file, String parent, boolean hasSelf) throws IOException {

			int ret = 0;

			if (!file.exists()) {
				return ret;
			}

			parent = (parent == null) ? "" : parent;

			if (file.isDirectory()) {
				buf.delete(0, buf.length());
				buf.append(parent).append(file.getName()).append("/");
				String zipEntryName = null;
				if (hasSelf) {
					zipEntryName = buf.toString();
					ZipEntry en = new ZipEntry(zipEntryName);
					en.setTime(file.lastModified());
					zout.putNextEntry(en);
					zout.closeEntry();
				}
				File[] files = file.listFiles();
				int n = files.length;
				for (int i = 0; i < n; i++) {
					ret += compress0(files[i], zipEntryName, true);
				}
			} else {
				// is a file
				buf.delete(0, buf.length());
				buf.append(parent).append(file.getName());
				String zipEntryName = buf.toString();
				ZipEntry en = new ZipEntry(zipEntryName);
				en.setTime(file.lastModified());
				zout.putNextEntry(en);

				RandomAccessFile rf = new RandomAccessFile(file, "r");
				int rb = -1;
				while ((rb = rf.read(dbf)) >= 0) {
					zout.write(dbf, 0, rb);
					ret += rb;
				}
				zout.closeEntry();
				rf.close();
			}
			return ret;
		}

		private int expand0(ZipEntry entry, String folder, boolean hasSelf) throws IOException {

			int ret = 0;

			if (entry == null) {
				return ret;
			}

			File file = new File(folder);
			if (!file.exists()) {
				file.mkdirs();
			}
			String path = file.getAbsolutePath();
			if (!path.endsWith(File.separator)) {
				path += File.separator;
			}

			String entryName = entry.getName();

			if (entry.isDirectory()) {
				String enName = entryName.substring(0, entryName.length() - 1);
				int p = enName.lastIndexOf("/");
				enName = enName.substring(p == -1 ? 0 : p + 1);
				String newFolder = path;
				if (hasSelf) {
					newFolder += enName;
					new File(newFolder).mkdir();
				}

				list.remove(entryName);

				Iterator it = getChildren(entryName);
				while (it.hasNext()) {
					enName = (String) it.next();
					ret += expand(enName, newFolder, true);
					it = getChildren(entryName);
				}

			} else {
				// is a file
				int p = entryName.lastIndexOf("/");
				String fileName = path + entryName.substring(p == -1 ? 0 : p + 1);
				BufferedInputStream bin = new BufferedInputStream(zin.getInputStream(entry));
				RandomAccessFile rf = new RandomAccessFile(fileName, "rw");
				int rb = -1;
				while ((rb = bin.read(dbf)) >= 0) {
					rf.write(dbf, 0, rb);
					ret += rb;
				}
				rf.close();
				bin.close();
				list.remove(entryName);
			}
			return ret;
		}

		private Iterator getChildren(String entryName) {
			ArrayList tmpList = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				String enName = (String) it.next();
				if (enName.startsWith(entryName)) {
					tmpList.add(enName);
				}
			}
			return tmpList.iterator();
		}

		/*
		 * public static void main(String[] args) throws IOException {
		 * ZipArchive zf = new ZipArchive();
		 * 
		 * zf.open("c:\\test.jar", MODE_WRITE); zf.compress("c:\\temp", "/",
		 * true);
		 * 
		 * //zf.open("c:\\test.jar", MODE_READ); //zf.expand("/", "c:\\temp",
		 * true);
		 * 
		 * zf.close();
		 * 
		 * }
		 */

	}
}
