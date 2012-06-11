package com.kmetop.demsy.modules.webmail.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.kmetop.demsy.modules.webmail.enums.MailboxProtocolEnum;
import com.kmetop.demsy.modules.webmail.spi.Configuration;
import com.kmetop.demsy.modules.webmail.spi.Lifecycle;

public class LoginModel implements Lifecycle, Serializable {

	private static final long serialVersionUID = 1129798977717127867L;

	private static Map<MailboxProtocolEnum, Integer> protocolPorts = new HashMap<MailboxProtocolEnum, Integer>();

	private MailboxProtocolEnum mailboxProtocol = null;// 协议

	private String mailboxHost = Configuration.getPreselectionMailboxHost();// 主机

	private int mailboxPort = (-1);// 端口

	private String mailboxUser = null;// 用户

	private String mailboxPassword = null;// 密码

	private boolean advancedLogonProperties = false;// 高几登录选项

	static {

		protocolPorts.put(MailboxProtocolEnum.POP3,
				(new Integer(Configuration.getPortnumberPop3())));
		protocolPorts.put(MailboxProtocolEnum.IMAP,
				(new Integer(Configuration.getPortnumberImap())));
		protocolPorts.put(MailboxProtocolEnum.POP3_SSL, (new Integer(Configuration
				.getPortnumberPop3s())));
		protocolPorts.put(MailboxProtocolEnum.IMAP_SSL, (new Integer(Configuration
				.getPortnumberImaps())));
	}

	public LoginModel() {

		this.setMailboxProtocol(MailboxProtocolEnum.byProtocolId(Configuration
				.getPreselectionMailboxProtocol()));
	}

	// --------------------------------------------------------- Getter und
	// Setter

	/**
	 * @return Returns the mailboxPort.
	 */
	public int getMailboxPort() {
		return mailboxPort;
	}

	/**
	 * @param mailboxPort
	 *            The mailboxPort to set.
	 */
	public void setMailboxPort(int mailboxPort) {
		this.mailboxPort = mailboxPort;
	}

	/**
	 * @return Returns the mailboxProtocol.
	 */
	public MailboxProtocolEnum getMailboxProtocol() {
		return mailboxProtocol;
	}

	/**
	 * @param mailboxProtocol
	 *            The mailboxProtocol to set.
	 */
	public void setMailboxProtocol(MailboxProtocolEnum mailboxProtocol) {

		this.mailboxProtocol = mailboxProtocol;

		// Wenn wir uns NICHT im "Advanced mode" befinden, passen wir auch
		// gleich
		// den Port an
		if (!this.advancedLogonProperties) {

			this.mailboxPort = ((Integer) protocolPorts.get(mailboxProtocol)).intValue();
		}
	}

	/**
	 * @return Returns the mailboxHost.
	 */
	public String getMailboxHost() {
		return mailboxHost;
	}

	/**
	 * @param mailboxHost
	 *            The mailboxHost to set.
	 */
	public void setMailboxHost(String mailboxHost) {
		this.mailboxHost = mailboxHost;
	}

	/**
	 * @return Returns the mailboxPassword.
	 */
	public String getMailboxPassword() {
		return mailboxPassword;
	}

	/**
	 * @param mailboxPassword
	 *            The mailboxPassword to set.
	 */
	public void setMailboxPassword(String mailboxPassword) {
		this.mailboxPassword = mailboxPassword;
	}

	/**
	 * @return Returns the mailboxUser.
	 */
	public String getMailboxUser() {
		return mailboxUser;
	}

	/**
	 * @param mailboxUser
	 *            The mailboxUser to set.
	 */
	public void setMailboxUser(String mailboxUser) {
		this.mailboxUser = mailboxUser;
	}

	/**
	 * @return the advancedLogonProperties
	 */
	public boolean isAdvancedLogonProperties() {

		return advancedLogonProperties;
	}

	/**
	 * @param advancedLogonProperties
	 *            the advancedLogonProperties to set
	 */
	public void setAdvancedLogonProperties(boolean advancedLogonProperties) {

		this.advancedLogonProperties = advancedLogonProperties;
	}

	// ----------------------------------------------------- oeffentliche
	// Methoden

	/**
	 * Setzt die Bean zurueck
	 */
	public void reset() {

		// Defaultmaessig befinden wir uns nicht im "Advanced mode"
		this.advancedLogonProperties = false;

		// Protocol und Port zuruecksetzen
		this.setMailboxProtocol(MailboxProtocolEnum.byProtocolId(Configuration
				.getPreselectionMailboxProtocol()));

		// Andere Properties zuruecksetzen
		this.mailboxHost = Configuration.getPreselectionMailboxHost();
		this.mailboxUser = null;
		this.mailboxPassword = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Lifecycle#destroy()
	 */
	public void destroy() {

		this.reset();
	}

}
