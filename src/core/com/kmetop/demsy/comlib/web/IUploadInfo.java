package com.kmetop.demsy.comlib.web;

import java.util.Date;

import com.kmetop.demsy.comlib.biz.IBizField;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.security.IModule;

/**
 * 上传文件信息接口
 * 
 * @author yongshan.ji
 * 
 */
public interface IUploadInfo {

	/**
	 * 获取上传时的本地文件名
	 * 
	 * @return 返回原始文件名称
	 */
	String getLocalName();

	/**
	 * 获取文件相对路径
	 * 
	 * @return 返回SERVLET环境下的文件相对路径
	 */
	Upload getPath();

	/**
	 * 获取谁上传的该文件
	 * 
	 * @return 用户帐号
	 */
	String getUploadBy();

	/**
	 * 获取什么时候上传的该文件
	 * 
	 * @return 上传日期
	 */
	Date getUploadDate();

	/**
	 * 获取上传文件内容字节大小
	 * 
	 * @return
	 */
	Long getContentLength();

	/**
	 * 获取上传文件内容类型
	 * 
	 * @return
	 */
	String getContentType();

	void setSoftID(Long id);

	void setContentType(String contentType);

	void setExtName(String fileExtension);

	void setLocalName(String fileLocalName);

	void setContentLength(long length);

	void setPath(Upload path);

	void setModule(IModule module);

	void setBzfield(IBizField field);

}
