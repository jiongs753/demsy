package com.kmetop.demsy.actions;

import static com.kmetop.demsy.mvc.MvcConst.VW_BIZ;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.biz.BizConst;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.field.RichText;
import com.kmetop.demsy.comlib.eshop.IProduct;
import com.kmetop.demsy.comlib.web.IBbsReply;
import com.kmetop.demsy.comlib.web.IBbsTopic;
import com.kmetop.demsy.comlib.web.IWebContent;
import com.kmetop.demsy.comlib.web.IWebContentCatalog;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.lang.Http;
import com.kmetop.demsy.lang.Img;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.mvc.MvcConst;
import com.kmetop.demsy.mvc.nutz.DemsyAdaptor;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.orm.Pager;
import com.kmetop.demsy.orm.expr.CndExpr;
import com.kmetop.demsy.orm.expr.Expr;

@Ok(VW_BIZ)
@Fail(VW_BIZ)
@AdaptBy(type = DemsyAdaptor.class)
public class IPhoneActions extends ModuleActions implements BizConst, MvcConst {

	/**
	 * 输出新闻列表（.plist 格式的XML内容）到iPhone设备
	 * 
	 * @throws DemsyException
	 */
	@At("/ui/iphone/plistWebInfo/*")
	public void plistWebInfo(int imageWidth, int imageHeight, int pageIndex) {
		// IBizManager bizManager = getBizManager(IWebInfo.SYS_CODE);
		IOrm orm = Demsy.orm();

		// 获取支持检索的网站栏目
		Class webInfoCatalogType = Demsy.bizEngine.getType(IWebContentCatalog.SYS_CODE);
		List webInfoCatalogList = Demsy.orm().query(webInfoCatalogType,
				Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft().getId()).and(Expr.eq("infoEnabledSearch", 1)));

		CndExpr expr = Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft().getId())
		// 软件标识
				.and(Expr.in("catalog", webInfoCatalogList))
				//
				.and(Expr.eq("status", 1))
				// 支持检索的栏目
				.and(Expr.isNull("refrence"))
				// 非推荐的信息
				.and(Expr.notNull("image"))
				// 信息中包含图片
				.and(Expr.ne("image", ""))
				// 信息中包含图片
				.and(Expr.notNull("content"))//
				// .and(Expr.ne("content", ""))
				.addDesc(LibConst.F_UPDATED);// 排序
		Class webInfoType = Demsy.bizEngine.getType(IWebContent.SYS_CODE);
		Pager pager = Pager.make(webInfoType, 20, pageIndex, expr);
		List<IWebContent> result = orm.query(pager);

		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
		xml.append("<plist version=\"1.0\">");
		xml.append("<dict>");
		xml.append("<key>pageIndex</key>");
		xml.append("<string>").append(pager.getPageIndex()).append("</string>");
		xml.append("<key>totalRecords</key>");
		xml.append("<string>").append(pager.getTotalRecord()).append("</string>");
		xml.append("<key>rows</key>");
		xml.append("<array>");
		for (IWebContent info : result) {
			xml.append("<dict>");
			// id
			xml.append("<key>id</key>");
			xml.append("<string>").append(info.getInfoID()).append("</string>");
			// image
			xml.append("<key>image</key>");
			String srcImage = info.getInfoImage();
			xml.append("<string><![CDATA[").append(this.zoomImage(srcImage, imageWidth, imageHeight, true))
					.append("]]></string>");

			// title
			xml.append("<key>title</key>");
			xml.append("<string><![CDATA[").append(info.getName()).append("]]></string>");
			// desc
			xml.append("<key>summary</key>");
			xml.append("<string><![CDATA[").append(info.getInfoDesc()).append("]]></string>");
			// update date
			xml.append("<key>updated</key>");
			xml.append("<string>").append(info.getInfoDate()).append("</string>");

			xml.append("</dict>");
		}
		xml.append("</array>");
		xml.append("</dict>");
		xml.append("</plist>");

		try {
			Http.writeXml(xml.toString());
		} catch (IOException e) {
			log.error("输出XML到iPhone端出错！", e);
		}
	}

	/**
	 * 输出指定id号的新闻对象详细内容
	 * 
	 * @param id
	 *            新闻对象ID
	 */
	@At("/ui/iphone/detailWebInfo/*")
	public void detailWebInfo(Long id) throws DemsyException {
		Class type = Demsy.bizEngine.getType(IWebContent.SYS_CODE);
		IOrm orm = Demsy.orm();
		IWebContent info = (IWebContent) orm.load(type, id);
		IWebContent refInfo = info.getRefrence();
		if (refInfo != null) {
			info = (IWebContent) orm.load(type, refInfo.getInfoID());
		}
		RichText content = info.getInfoContent();
		try {
			String str = "";
			if (content != null) {
				str = content.toString();
			}

			Http.writeHtml(showInIPhoneWebView(str));
		} catch (IOException e) {
			log.error("输出新闻内容到iPhone端出错！", e);
		}
	}

	/**
	 * 输出产品列表（ .plist 格式的XML内容）到iPhone设备
	 * 
	 * @throws DemsyException
	 */
	@At("/ui/iphone/plistProduct/*")
	public void plistProduct(int imageWidth, int imageHeight, int pageIndex) {
		IOrm orm = Demsy.orm();

		CndExpr expr = Expr.eq(LibConst.F_SOFT_ID, Demsy.me().getSoft().getId()).and(Expr.notNull("image"))
		// 信息中包含图片
				.and(Expr.ne("image", ""))
				// 信息中包含图片
				.and(Expr.notNull("content"))//
				// .and(Expr.ne("content", ""))
				.addDesc("allowBuy").addDesc(LibConst.F_UPDATED);// 排序
		Class type = Demsy.bizEngine.getType(IProduct.SYS_CODE);
		Pager pager = Pager.make(type, 20, pageIndex, expr);
		List<IProduct> result = orm.query(pager);

		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
		xml.append("<plist version=\"1.0\">");
		xml.append("<dict>");
		xml.append("<key>pageIndex</key>");
		xml.append("<string>").append(pager.getPageIndex()).append("</string>");
		xml.append("<key>totalRecords</key>");
		xml.append("<string>").append(pager.getTotalRecord()).append("</string>");
		xml.append("<key>rows</key>");
		xml.append("<array>");
		for (IProduct info : result) {
			xml.append("<dict>");
			// id
			xml.append("<key>id</key>");
			xml.append("<string>").append(info.getId()).append("</string>");
			// image
			xml.append("<key>image</key>");
			String srcImage = null;
			if (info.getImage() != null) {
				srcImage = info.getImage().toString();
			}
			xml.append("<string><![CDATA[").append(this.zoomImage(srcImage, imageWidth, imageHeight, true))
					.append("]]></string>");

			// title
			xml.append("<key>title</key>");
			xml.append("<string><![CDATA[").append(info.getName()).append("]]></string>");
			// desc
			xml.append("<key>summary</key>");
			xml.append("<string>");

			// Double oldPrice = info.getOldPrice();
			Double nowPrice = info.getNowPrice();
			// Double balence = info.getBalance();
			if (nowPrice != null) {
				xml.append("￥").append(Obj.format(nowPrice, "##0.00"));
			}
			xml.append("</string>");

			xml.append("</dict>");
		}
		xml.append("</array>");
		xml.append("</dict>");
		xml.append("</plist>");

		try {
			Http.writeXml(xml.toString());
		} catch (IOException e) {
			log.error("输出产品XML到iPhone端出错！", e);
		}
	}

	/**
	 * 输出指定id号的产品对象详细内容
	 * 
	 * @param id
	 *            产品对象ID
	 */
	@At("/ui/iphone/detailProduct/*")
	public void htmlWebInfo(Long id) throws DemsyException {
		Class type = Demsy.bizEngine.getType(IProduct.SYS_CODE);
		IOrm orm = Demsy.orm();
		IProduct info = (IProduct) orm.load(type, id);
		RichText content = info.getContent();
		try {
			String str = "";
			if (content != null) {
				str = content.toString();
			}

			Http.writeHtml(showInIPhoneWebView(str));
		} catch (IOException e) {
			log.error("输出产品详细信息到iPhone端出错！", e);
		}
	}

	/**
	 * 输出论坛帖子列表（ .plist 格式的XML内容）到iPhone设备
	 * 
	 * @throws DemsyException
	 */
	@At("/ui/iphone/plistBbs/*")
	public void plistBbs(int pageIndex) {
		IOrm orm = Demsy.orm();

		CndExpr expr = Expr.ne("status", 1).addDesc("created");// 排序
		Class type = Demsy.bizEngine.getType(IBbsTopic.SYS_CODE);
		Pager pager = Pager.make(type, 20, pageIndex, expr);
		List<IBbsTopic> result = orm.query(pager);

		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
		xml.append("<plist version=\"1.0\">");
		xml.append("<dict>");
		xml.append("<key>pageIndex</key>");
		xml.append("<string>").append(pager.getPageIndex()).append("</string>");
		xml.append("<key>totalRecords</key>");
		xml.append("<string>").append(pager.getTotalRecord()).append("</string>");
		xml.append("<key>rows</key>");
		xml.append("<array>");
		for (IBbsTopic info : result) {
			xml.append("<dict>");
			// id
			xml.append("<key>id</key>");
			xml.append("<string>").append(info.getId()).append("</string>");

			// title
			xml.append("<key>title</key>");
			xml.append("<string>").append(info.getName()).append("</string>");

			// desc
			xml.append("<key>summary</key>");
			xml.append("<string>").append(info.getDesc()).append("</string>");

			xml.append("</dict>");
		}
		xml.append("</array>");
		xml.append("</dict>");
		xml.append("</plist>");

		try {
			Http.writeXml(xml.toString());
		} catch (IOException e) {
			log.error("输出产品XML到iPhone端出错！", e);
		}
	}

	/**
	 * 输出论坛回帖列表（ .plist 格式的XML内容）到iPhone设备
	 * 
	 * @throws DemsyException
	 */
	@At("/ui/iphone/plistBbsReply/*")
	public void plistBbsReply(int topicID, int pageIndex) {
		IOrm orm = Demsy.orm();

		CndExpr expr = Expr.ne("status", 1)//
				.and(Expr.eq("topic", topicID)).addDesc("created");// 排序
		Class type = Demsy.bizEngine.getType(IBbsReply.SYS_CODE);
		Pager pager = Pager.make(type, 20, pageIndex, expr);
		List<IBbsTopic> result = orm.query(pager);

		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
		xml.append("<plist version=\"1.0\">");
		xml.append("<dict>");
		xml.append("<key>pageIndex</key>");
		xml.append("<string>").append(pager.getPageIndex()).append("</string>");
		xml.append("<key>totalRecords</key>");
		xml.append("<string>").append(pager.getTotalRecord()).append("</string>");
		xml.append("<key>rows</key>");
		xml.append("<array>");
		for (IBbsTopic info : result) {
			xml.append("<dict>");
			// id
			xml.append("<key>id</key>");
			xml.append("<string>").append(info.getId()).append("</string>");

			// title
			xml.append("<key>title</key>");
			xml.append("<string>").append(info.getName()).append("</string>");
			// desc
			xml.append("<key>summary</key>");
			xml.append("<string>").append(info.getDesc()).append("</string>");

			xml.append("</dict>");
		}
		xml.append("</array>");
		xml.append("</dict>");
		xml.append("</plist>");

		try {
			Http.writeXml(xml.toString());
		} catch (IOException e) {
			log.error("输出产品XML到iPhone端出错！", e);
		}
	}

	private String showInIPhoneWebView(String str) {
		str = str.toLowerCase();
		StringBuffer sb = new StringBuffer();
		// 压缩图片 使其宽度和iPhone宽度一致
		int imgStart = str.indexOf("<img ");
		int imageWidth = 300;
		int imageHeight = 460;
		while (imgStart > -1) {
			// 添加图片前面的内容
			sb.append(str.substring(0, imgStart));

			str = str.substring(imgStart);

			int imgEnd = str.indexOf(">");

			/*
			 * 处理图片
			 */
			String imgTag = str.substring(0, imgEnd);

			int imgSrcFrom = imgTag.indexOf("src=");
			if (imgSrcFrom > -1) {
				int imgSrcTo = -1;
				imgTag = imgTag.substring(imgSrcFrom + 4).trim();
				if (imgTag.startsWith("'")) {
					imgTag = imgTag.substring(1);
					imgSrcTo = imgTag.indexOf("'");
				} else if (imgTag.startsWith("\"")) {
					imgTag = imgTag.substring(1);
					imgSrcTo = imgTag.indexOf("\"");
				}

				String srcImage = "";
				if (imgSrcTo > -1) {
					srcImage = imgTag.substring(0, imgSrcTo);
				}
				if (srcImage.length() > 0) {
					String img = zoomImage(srcImage, imageWidth, imageHeight, false);
					sb.append("<img src=\"").append(img).append("\" />");
				}
			}
			// 处理图片结束

			str = str.substring(imgEnd + 1);
			imgStart = str.indexOf("<img ");
		}
		sb.append(str);

		return sb.toString();
	}

	private String zoomImage(String srcImage, int imageWidth, int imageHeight, boolean autoCutImage) {
		String img = "";
		if (srcImage == null) {
			return "";
		}
		if (srcImage.startsWith("http://")) {
			srcImage = srcImage.substring(7);
			srcImage = srcImage.substring(srcImage.indexOf("/"));
		} else if (srcImage.startsWith("https://")) {
			srcImage = srcImage.substring(8);
			srcImage = srcImage.substring(srcImage.indexOf("/"));
		}
		if (srcImage.startsWith("/")) {
			img = Img.zoomImgPath(srcImage, imageWidth, imageHeight).replace(" ", "_");
			String targetImg = Demsy.contextDir + img;
			String srcImg = Demsy.contextDir + srcImage;
			if (!new File(targetImg).exists() && new File(srcImg).exists()) {
				Img.zoomImage(srcImg, targetImg, imageWidth, imageHeight, autoCutImage);
			}
		}

		return img;
	}
}
