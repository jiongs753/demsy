package com.kmetop.demsy.lang;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片工具
 * 
 * @author yongshan.ji
 */
public abstract class Img {
	private final static String[] IMAGEARRAY = { "jpg", "jpeg", "gif", "png", "bmp" };

	/**
	 * 获取图片尺寸
	 * 
	 * @param path
	 *            0：width, 1:height
	 * @return
	 * @throws IOException
	 */
	public static int[] size(String path) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		return new int[] { image.getWidth(), image.getHeight() };
	}

	public static String zoomImgPath(String imgSrc, int width, int height) {
		if (imgSrc == null) {
			return "";
		}
		int dot = imgSrc.lastIndexOf(".");
		String ext = "";
		if (dot > 0) {
			ext = imgSrc.substring(dot);
			return "/thumbs" + imgSrc.substring(0, dot) + "_" + width + "_" + height + ext;
		} else
			return imgSrc;
	}

	public static String cutImgName(String imgSrc, int width, int height) {
		if (imgSrc == null) {
			return "";
		}
		int dot = imgSrc.lastIndexOf(".");
		String ext = "";
		if (dot > 0) {
			ext = imgSrc.substring(dot);
			return imgSrc.substring(0, dot) + "_" + width + "_" + height + ext;
		} else
			return imgSrc;
	}

	public static boolean zoomImage(String srcImg, String destImg, int destWidth, int destHeight, boolean autoCutImage) {
		FileOutputStream fos = null;
		try {
			File destFile = new File(destImg);
			destFile.getParentFile().mkdirs();
			File srcFile = new File(srcImg);

			Image image = javax.imageio.ImageIO.read(srcFile);
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
			if (imageWidth <= destWidth && imageHeight <= destHeight) {
				Files.copyFile(srcFile, destFile);
				return true;
			}

			// 剪切图片
			if (autoCutImage) {
				double newScale = new Double(destWidth) / new Double(destHeight);
				double srcScale = new Double(imageWidth) / new Double(imageHeight);
				if (newScale > srcScale) {// 原图片太高
					double cutHeight = imageHeight - imageWidth / newScale;
					int newHeight = new Double(imageHeight - cutHeight).intValue();
					int newY = (imageHeight - newHeight) / 2;

					String cutImgName = cutImgName(srcFile.getName(), imageWidth, newHeight);
					String newSrcImg = destFile.getParentFile().getAbsolutePath() + File.separator + cutImgName;
					cutImage(srcImg, newSrcImg, 0, newY, imageWidth, newHeight);
					srcImg = newSrcImg;
				} else if (newScale < srcScale) {// 原图片太长
					double cutWidth = imageWidth - imageHeight * newScale;
					int newWidth = new Double(imageWidth - cutWidth).intValue();
					int newX = (imageWidth - newWidth) / 2;

					String cutImgName = cutImgName(srcFile.getName(), newWidth, imageHeight);
					String newSrcImg = destFile.getParentFile().getAbsolutePath() + File.separator + cutImgName;
					cutImage(srcImg, newSrcImg, newX, 0, newWidth, imageHeight);
					srcImg = newSrcImg;
				}

				// 压缩图片
				srcFile = new File(srcImg);
				image = javax.imageio.ImageIO.read(srcFile);
				imageWidth = image.getWidth(null);
				imageHeight = image.getHeight(null);
			}

			float scale = getRatio(imageWidth, imageHeight, destWidth, destHeight);
			imageWidth = (int) (scale * imageWidth);
			imageHeight = (int) (scale * imageHeight);

			image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
			// Make a BufferedImage from the Image.
			BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = mBufferedImage.createGraphics();

			// Map readeringHint = new HashMap();
			// readeringHint.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
			// RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			// readeringHint.put(RenderingHints.KEY_ANTIALIASING,
			// RenderingHints.VALUE_ANTIALIAS_ON);
			// readeringHint.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			// readeringHint.put(RenderingHints.KEY_DITHERING,
			// RenderingHints.VALUE_DITHER_ENABLE);
			// readeringHint.put(RenderingHints.KEY_INTERPOLATION,
			// RenderingHints.VALUE_INTERPOLATION_BILINEAR);//VALUE_INTERPOLATION_BICUBIC
			// readeringHint.put(RenderingHints.KEY_RENDERING,
			// RenderingHints.VALUE_RENDER_QUALITY);
			// g.setRenderingHints(readeringHint);

			g2.drawImage(image, 0, 0, imageWidth, imageHeight, Color.white, null);
			g2.dispose();

			float[] kernelData2 = { -0.125f, -0.125f, -0.125f, -0.125f, 2, -0.125f, -0.125f, -0.125f, -0.125f };
			Kernel kernel = new Kernel(3, 3, kernelData2);
			ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
			mBufferedImage = cOp.filter(mBufferedImage, null);

			fos = new FileOutputStream(destImg);
			// JPEGEncodeParam param =
			// encoder.getDefaultJPEGEncodeParam(bufferedImage);
			// param.setQuality(0.9f, true);
			// encoder.setJPEGEncodeParam(param);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
			encoder.encode(mBufferedImage);
			return true;
		} catch (FileNotFoundException fnf) {
			return false;
		} catch (IOException ioe) {
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/** 截取图片 */
	public static boolean cutImage(String file, String targetFile, int width, int height) {
		return cutImage(file, targetFile, 0, 0, width, height);
	}

	/** 截取图片 */
	public static boolean cutImage(String filePath, String targetFilePath, int x, int y, int width, int height) {
		File file = new File(filePath);
		File targetFile = new File(targetFilePath);
		FileOutputStream fos = null;
		ImageOutputStream ios = null;
		FileInputStream fis = null;
		ImageInputStream iis = null;
		try {
			String endName = file.getName();
			endName = endName.substring(endName.lastIndexOf(".") + 1);
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(endName);
			ImageReader reader = (ImageReader) readers.next();
			fis = new FileInputStream(file);
			iis = ImageIO.createImageInputStream(fis);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, width, height);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			if (!targetFile.exists()) {
				targetFile.getParentFile().mkdirs();
				targetFile.createNewFile();
			}
			fos = new FileOutputStream(targetFile);
			ios = ImageIO.createImageOutputStream(fos);
			ImageIO.write(bi, endName, ios);
		} catch (Exception e) {
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
			if (ios != null) {
				try {
					ios.close();
				} catch (IOException e) {
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
				}
			}
		}

		return true;
	}

	public static float getRatio(int width, int height, int maxWidth, int maxHeight) {
		float ratio = 1.0f;
		float widthRatio;
		float heightRatio;
		widthRatio = (float) maxWidth / width;
		heightRatio = (float) maxHeight / height;
		if (widthRatio < 1.0 || heightRatio < 1.0) {
			ratio = widthRatio <= heightRatio ? widthRatio : heightRatio;
		}
		return ratio;
	}

	public static boolean isImage(String filepath) {
		if (filepath == null)
			return false;

		int dot = filepath.lastIndexOf(".");
		if (dot > -1) {
			filepath = filepath.substring(dot + 1);
		}
		return isExtension(filepath, IMAGEARRAY);
	}

	public static boolean isExtension(String fileExt, String[] extensions) {
		if (fileExt == null || fileExt.length() == 0) {
			return false;
		} else {
			for (int i = 0; i < extensions.length; i++) {
				if (fileExt.toLowerCase().equals(extensions[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 把图片印刷到图片上
	 * 
	 * @param pressImg
	 *            -- 水印文件
	 * @param targetImg
	 *            -- 目标文件
	 * @param x
	 *            --x坐标
	 * @param y
	 *            --y坐标
	 * @throws IOException
	 */
	public final static void pressImage(String pressImg, String targetImg, int x, int y) throws IOException {
		// 目标文件
		File file = new File(targetImg);
		Image src = ImageIO.read(file);
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		File logoFile = new File(pressImg);
		Image logoSrc = ImageIO.read(logoFile);
		int logoWidth = logoSrc.getWidth(null);
		int logoHeight = logoSrc.getHeight(null);
		if (width < 500) {
			return;
		}

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		g.drawImage(src, 0, 0, width, height, null);

		// 水印文件
		// int ratio = logoWidth / logoHeight;
		// int w = width / 2;
		// if (w < logoWidth) {
		// logoWidth = w;
		// }
		// if (logoWidth % 2 == 1) {
		// logoWidth = logoWidth - 1;
		// }
		// logoHeight = logoWidth / ratio;
		g.drawImage(logoSrc, (width - logoWidth), (height - logoHeight), logoWidth, logoHeight, null);
		// 水印文件结束
		g.dispose();
		FileOutputStream out = new FileOutputStream(targetImg);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(image);
		out.close();
	}

	/**
	 * 打印文字水印图片
	 * 
	 * @param pressText
	 *            --文字
	 * @param targetImg
	 *            -- 目标图片
	 * @param fontName
	 *            -- 字体名
	 * @param fontStyle
	 *            -- 字体样式
	 * @param color
	 *            -- 字体颜色
	 * @param fontSize
	 *            -- 字体大小
	 * @param x
	 *            -- 偏移量
	 * @param y
	 * @throws IOException
	 */

	public static void pressText(String pressText, String targetImg, String fontName, int fontStyle, int color, int fontSize, int x, int y) throws IOException {
		File _file = new File(targetImg);
		Image src = ImageIO.read(_file);
		int wideth = src.getWidth(null);
		int height = src.getHeight(null);
		BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		g.drawImage(src, 0, 0, wideth, height, null);
		// String s="www.qhd.com.cn";
		g.setColor(Color.RED);
		g.setFont(new Font(fontName, fontStyle, fontSize));

		g.drawString(pressText, wideth - fontSize - x, height - fontSize / 2 - y);
		g.dispose();
		FileOutputStream out = new FileOutputStream(targetImg);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(image);
		out.close();
	}

}
