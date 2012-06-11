package com.kmetop.demsy.modules.webmail.business;

import java.util.Properties;
import java.util.TreeSet;

import javax.mail.AuthenticationFailedException;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.net.ssl.SSLHandshakeException;

import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.modules.webmail.MailboxConnection;
import com.kmetop.demsy.modules.webmail.enums.MailboxProtocolEnum;
import com.kmetop.demsy.modules.webmail.exceptions.AccessDeniedException;
import com.kmetop.demsy.modules.webmail.exceptions.ConnectionEstablishException;
import com.kmetop.demsy.modules.webmail.exceptions.DemsyCertificateException;
import com.kmetop.demsy.modules.webmail.exceptions.LogoutException;
import com.kmetop.demsy.modules.webmail.exceptions.MailboxFolderException;
import com.kmetop.demsy.modules.webmail.exceptions.MessageDeletionException;
import com.kmetop.demsy.modules.webmail.exceptions.MessageMovementException;
import com.kmetop.demsy.modules.webmail.exceptions.MessageRetrieveException;
import com.kmetop.demsy.modules.webmail.model.LoginModel;
import com.kmetop.demsy.modules.webmail.model.RetrieveMessagesResultModel;
import com.kmetop.demsy.modules.webmail.spi.Constants;
import com.kmetop.demsy.modules.webmail.util.JavamailUtils;

/**
 * 邮箱连接器的默认实现
 */
public class DefaultMailboxConnection implements MailboxConnection {
	protected static final Log Log = Logs.getLog(DefaultMailboxConnection.class);

	private static final long serialVersionUID = -8364316467231432686L;

	private LoginModel loginModel = null;

	private Store store = null;

	private Folder folder = null;

	private Character separator = null;

	private TreeSet allFolders = null;

	protected DemsyTrustManager trustManager = null;

	public DefaultMailboxConnection(LoginModel loginData, DemsyTrustManager trustManager) {
		this.loginModel = loginData;
		this.trustManager = trustManager;
	}

	private void createStore() {
		try {
			// 当前使用的登录协议
			MailboxProtocolEnum curMbPrtcl = this.loginModel.getMailboxProtocol();

			// 获取系统属性
			Properties props = JavamailUtils.getProperties();

			// TODO:是否需要使用安全连接
			// if (curMbPrtcl.isUseOfSsl()) {
			// MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
			// socketFactory.setTrustManagers(new TrustManager[] {
			// this.trustManager });
			//
			// props.put(("mail." + curMbPrtcl.getProtocolId() +
			// ".ssl.socketFactory"),
			// socketFactory);
			// }

			// 获取邮件会话
			Session session = Session.getInstance(props, null);
			session.setDebug(false);

			this.store = session.getStore(curMbPrtcl.getProtocolId());
		} catch (NoSuchProviderException e) {
			throw (new RuntimeException(("未知协议: " + this.loginModel.getMailboxProtocol()), e));

			// TODO:是否需要使用安全连接
			// } catch (GeneralSecurityException gse) {
			// throw (new RuntimeException(("安全问题: " + gse.getMessage()), gse));

		}
	}

	private void openFolder(int mode) throws MailboxFolderException {
		try {
			this.folder.open(mode);
		} catch (MessagingException e) {
			throw (new MailboxFolderException(("打开文件夹 " + this.folder.getName() + " 出错"), e,
					this.folder.getName()));
		}
	}

	// 更新所有文件夹
	private void updateAllFoldersTreeSet() throws MessagingException {
		Folder[] allFoldersArray = this.store.getDefaultFolder().list("*");
		TreeSet<Folder> allFoldersTreeSet = new TreeSet<Folder>(new FolderByFullNameComparator());

		for (int ii = 0; ii < allFoldersArray.length; ii++) {

			allFoldersTreeSet.add(allFoldersArray[ii]);
		}

		this.allFolders = allFoldersTreeSet;
	}

	private RetrieveMessagesResultModel assembleRetrieveMessagesResult(Message[] messages,
			int overallMessageCount) {

		RetrieveMessagesResultModel result = new RetrieveMessagesResultModel();
		result.setMessages(messages);
		result.setOverallMessageCount(overallMessageCount);

		return (result);
	}

	public void setLoginModel(LoginModel loginData) {
		this.loginModel = loginData;
	}

	/**
	 * 登录
	 */
	public void login() throws ConnectionEstablishException, AccessDeniedException,
			DemsyCertificateException {

		if (this.store == null) {

			this.createStore();
		}

		// 登录
		try {
			this.store.connect(this.loginModel.getMailboxHost(), this.loginModel.getMailboxPort(),
					this.loginModel.getMailboxUser(), this.loginModel.getMailboxPassword());

			// 邮件系统文件夹分隔符
			if (this.separator == null) {
				this.separator = Character.valueOf(this.store.getDefaultFolder().getSeparator());
			}

			// 所有文件夹
			if (this.allFolders == null) {
				this.updateAllFoldersTreeSet();
			}
		} catch (AuthenticationFailedException afe) {
			throw (new AccessDeniedException("认证失败! ", afe));
		} catch (MessagingException me) {
			Exception nextException = me.getNextException();

			if ((nextException != null) && (nextException instanceof SSLHandshakeException)) {

				Throwable cause = ((SSLHandshakeException) nextException).getCause();

				if ((cause != null) && (cause instanceof DemsyCertificateException)) {

					throw ((DemsyCertificateException) cause);
				}
			}

			throw (new ConnectionEstablishException("建立连接失败! ", me,
					this.loginModel.getMailboxHost(), this.loginModel.getMailboxPort()));
		}
	}

	/**
	 * 登录到指定文件夹
	 */
	public void login(String folderName) throws ConnectionEstablishException,
			AccessDeniedException, MailboxFolderException, DemsyCertificateException {

		this.login();
		this.changeFolder(folderName);
	}

	public void logout() throws LogoutException {

		if ((this.folder != null) && this.folder.isOpen()) {

			try {

				this.folder.close(true);
			} catch (MessagingException me) {

				throw (new LogoutException("Problem beim Schliessen des Folders.", me));
			}

			this.folder = null;
		}

		if (this.store != null) {

			try {

				this.store.close();
			} catch (MessagingException me) {

				throw (new LogoutException("Problem beim Schliessen des Stores.", me));
			}

			this.store = null;
		}
	}

	public void validateLoginData() throws ConnectionEstablishException, AccessDeniedException,
			DemsyCertificateException {

		this.login();

		try {
			this.logout();
		} catch (LogoutException e) {
			Log.error("校验登录失败! ", e);
		}
	}

	public Folder getCurrentFolder() throws MailboxFolderException {

		if (this.folder != null) {

			return (this.folder);
		} else {
			throw (new MailboxFolderException("文件夹不存在! ", null));
		}
	}

	public void changeFolder(String folderName) throws MailboxFolderException {

		try {

			if (Constants.LEERSTRING.equals(folderName)) {

				this.folder = this.store.getDefaultFolder();
			} else {

				this.folder = this.store.getFolder(folderName);
			}

			if (this.folder == null) {

				throw (new MailboxFolderException(("非法文件夹: " + folderName), null));
			}
		} catch (MessagingException me) {
			throw (new MailboxFolderException(("改变文件夹失败: " + folderName), me, folderName));
		}
	}

	public RetrieveMessagesResultModel getMessages() throws MailboxFolderException,
			MessageRetrieveException {

		// Open folder
		this.openFolder(Folder.READ_ONLY);

		// Get messages and return them
		try {

			return (this.assembleRetrieveMessagesResult(this.folder.getMessages(), this.folder
					.getMessageCount()));
		} catch (MessagingException me) {
			String tempExceptionMessage = ("不能从指定的文件夹接收消息 \"" + this.folder.getName() + "\".");
			throw (new MessageRetrieveException(tempExceptionMessage, me));
		}
	}

	public RetrieveMessagesResultModel getMessages(int aStartNumber, int anEndNumber,
			boolean adjustParameters) throws MailboxFolderException, MessageRetrieveException {

		// Open folder (should happen before getting the message-count)
		this.openFolder(Folder.READ_ONLY);

		try {

			// The given numbers may not be larger than the amount of messages
			int tempMessageCount = this.folder.getMessageCount();

			if ((aStartNumber > tempMessageCount) || (anEndNumber > tempMessageCount)) {

				if (adjustParameters) {

					// In case the start-number is bigger than the amount of
					// messages, we
					// return an empty Message-Array.
					if (aStartNumber > tempMessageCount) {

						return (this.assembleRetrieveMessagesResult((new Message[0]), this.folder
								.getMessageCount()));
					}

					if (anEndNumber > tempMessageCount) {

						anEndNumber = tempMessageCount;
					}
				} else {

					String tempExceptionMessage = "指定的消息序号号超出了消息总数! ";
					throw (new MessageRetrieveException(tempExceptionMessage));
				}
			}

			// Get messages and return them
			Message[] tempMessages = this.folder.getMessages(aStartNumber, anEndNumber);
			return (this
					.assembleRetrieveMessagesResult(tempMessages, this.folder.getMessageCount()));
		} catch (MessagingException me) {

			String tempExceptionMessage = ("不能从文件夹 \"" + this.folder.getName() + "\" 接收消息.");
			throw (new MessageRetrieveException(tempExceptionMessage, me));
		}
	}

	public Message getMessage(int messageNumber) throws MailboxFolderException,
			MessageRetrieveException {

		Message message = null;

		// Folder updaten und oeffnen
		this.openFolder(Folder.READ_ONLY);

		// Messages holen
		try {

			message = this.folder.getMessage(messageNumber);
		} catch (MessagingException me) {

			throw (new MessageRetrieveException(
					("无法从文件夹 " + this.folder.getName() + " 接收到消息 " + messageNumber), me));
		}

		return (message);
	}

	public RetrieveMessagesResultModel getEnvelopes() throws MailboxFolderException,
			MessageRetrieveException {

		try {
			if (this.folder.getParent() == null) {

				return (this.assembleRetrieveMessagesResult(new Message[0], 0));
			}

			RetrieveMessagesResultModel rmr = this.getMessages();
			Message[] messages = rmr.getMessages();

			if (messages.length >= 1) {

				FetchProfile fp = new FetchProfile();
				fp.add(FetchProfile.Item.ENVELOPE);
				this.folder.fetch(messages, fp);
			}

			return (rmr);
		} catch (MessagingException me) {

			throw (new MessageRetrieveException(("不能获取 \"" + this.folder.getName() + "\" 封包消息."),
					me));
		}
	}

	public RetrieveMessagesResultModel getEnvelopes(int aStartIndex, int anEndIndex,
			boolean adjustParameters) throws MailboxFolderException, MessageRetrieveException {

		try {

			if (this.folder.getParent() == null) {

				return (this.assembleRetrieveMessagesResult((new Message[0]), 0));
			}

			RetrieveMessagesResultModel rmr = this.getMessages(aStartIndex, anEndIndex,
					adjustParameters);
			Message[] messages = rmr.getMessages();

			if (messages.length >= 1) {

				FetchProfile fp = new FetchProfile();
				fp.add(FetchProfile.Item.ENVELOPE);
				this.folder.fetch(messages, fp);
			}

			return (rmr);
		} catch (MessagingException me) {

			throw (new MessageRetrieveException(("Konnte Envelopes aus Folder \""
					+ this.folder.getName() + "\" nicht beziehen."), me));
		}
	}

	public void setDeletedFlag(int messageNumber) throws MailboxFolderException,
			MessageDeletionException {
		int[] uebergabe = new int[1];
		uebergabe[0] = messageNumber;

		this.setMultipleDeletedFlags(uebergabe);
	}

	public void setMultipleDeletedFlags(int[] messageNumbers) throws MailboxFolderException,
			MessageDeletionException {

		this.openFolder(Folder.READ_WRITE);

		for (int ww = 0; ww < messageNumbers.length; ww++) {

			try {

				this.folder.getMessage(messageNumbers[ww]).setFlag(Flags.Flag.DELETED, true);
			} catch (MessagingException me) {

				throw (new MessageDeletionException("Probleme beim DELETED-Flags setzen", me));
			}
		}
	}

	public char getFolderSeparator() throws Exception {
		if (this.separator != null) {

			return (this.separator.charValue());
		}

		else {

			throw (new Exception("FolderSeparator nicht beziehbar."));
		}
	}

	public TreeSet getAllFolders() throws Exception {

		if (this.allFolders != null) {

			return (this.allFolders);
		}

		else {

			throw (new Exception("Ordnerliste nicht beziehbar."));
		}
	}

	public void createFolder(String folderName) throws MailboxFolderException {

		try {

			Folder newFolder = this.store.getFolder(folderName);

			if (!newFolder.exists()) {

				if (!newFolder.create(Folder.HOLDS_MESSAGES)) {

					throw (new Exception("Folder konnte nicht erstellt werden."));
				}

				this.updateAllFoldersTreeSet();
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw (new MailboxFolderException(e.getMessage(), e, folderName));
		}
	}

	public void deleteFolder(String folderName) throws MailboxFolderException {

		try {

			Folder folderToDelete = this.store.getFolder(folderName);

			if (folderToDelete.exists()) {

				if (!folderToDelete.delete(true)) {

					throw (new Exception("Folder konnte nicht geloescht werden."));
				}

				this.updateAllFoldersTreeSet();
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw (new MailboxFolderException(e.getMessage(), e, folderName));
		}
	}

	public void moveMessages(int[] messageNumbers, String targetFolderName)
			throws MailboxFolderException, MessageMovementException {
		try {
			this.openFolder(Folder.READ_WRITE);
			Message[] messages = new Message[messageNumbers.length];

			for (int ii = 0; ii < messageNumbers.length; ii++) {

				messages[ii] = this.folder.getMessage(messageNumbers[ii]);
			}

			Folder targetFolderObj = this.store.getFolder(targetFolderName);
			this.folder.copyMessages(messages, targetFolderObj);

			for (int kk = 0; kk < messages.length; kk++) {

				messages[kk].setFlag(Flags.Flag.DELETED, true);
			}
		} catch (MessagingException e) {

			throw (new MessageMovementException("Problem beim Verschieben.", e));
		}
	}

	public void destroy() {
		try {

			this.logout();
		} catch (LogoutException e) {
		}

		this.loginModel = null;
	}

}
