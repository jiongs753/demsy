package com.kmetop.demsy.modules.sms;

public interface ISmsEngine {

	/**
	 * 获取短信余额
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getBalance() throws Exception;

	/**
	 * 修改服务密码
	 * 
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 * @throws Exception
	 */
	public int modifyPassword(String newPwd) throws Exception;

	/**
	 * 解析短信
	 * 
	 * @param smsEntity
	 * @throws Exception
	 */
	public void parseSms(Object smsEntity) throws Exception;

	/**
	 * 发送短信
	 * 
	 * @param smsEntity
	 *            短信实体
	 * @throws Exception
	 */
	public void sendSms(Object smsEntity) throws Exception;

	/**
	 * 接收短信
	 */
	public void receiveSms() throws Exception;
}
