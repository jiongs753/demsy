package com.kmetop.demsy.modules.webmail;

import java.io.Serializable;
import java.util.TreeSet;

import javax.mail.Folder;
import javax.mail.Message;

import com.kmetop.demsy.modules.webmail.exceptions.AccessDeniedException;
import com.kmetop.demsy.modules.webmail.exceptions.ConnectionEstablishException;
import com.kmetop.demsy.modules.webmail.exceptions.LogoutException;
import com.kmetop.demsy.modules.webmail.exceptions.MailboxFolderException;
import com.kmetop.demsy.modules.webmail.exceptions.MessageDeletionException;
import com.kmetop.demsy.modules.webmail.exceptions.MessageMovementException;
import com.kmetop.demsy.modules.webmail.exceptions.MessageRetrieveException;
import com.kmetop.demsy.modules.webmail.exceptions.DemsyCertificateException;
import com.kmetop.demsy.modules.webmail.model.LoginModel;
import com.kmetop.demsy.modules.webmail.model.RetrieveMessagesResultModel;
import com.kmetop.demsy.modules.webmail.spi.Lifecycle;

public interface MailboxConnection extends Lifecycle, Serializable {
	/**
	 * 设置登录模型
	 * 
	 * @param loginModel
	 */
	public void setLoginModel(LoginModel loginModel);

	public void login() throws ConnectionEstablishException, AccessDeniedException,
			DemsyCertificateException;

	public void login(String folderName) throws ConnectionEstablishException,
			AccessDeniedException, MailboxFolderException, DemsyCertificateException;

	public void logout() throws LogoutException;

	public void validateLoginData() throws ConnectionEstablishException, AccessDeniedException,
			DemsyCertificateException;

	public Folder getCurrentFolder() throws MailboxFolderException;

	public void changeFolder(String folderName) throws MailboxFolderException;

	public RetrieveMessagesResultModel getMessages() throws MailboxFolderException,
			MessageRetrieveException;

	public RetrieveMessagesResultModel getMessages(int aStartNumber, int anEndNumber,
			boolean adjustParameters) throws MailboxFolderException, MessageRetrieveException;

	public Message getMessage(int messageNumber) throws MailboxFolderException,
			MessageRetrieveException;

	public RetrieveMessagesResultModel getEnvelopes() throws MailboxFolderException,
			MessageRetrieveException;

	public RetrieveMessagesResultModel getEnvelopes(int aStartNumber, int anEndNumber,
			boolean adjustParameters) throws MailboxFolderException, MessageRetrieveException;

	public void setDeletedFlag(int messageNumber) throws MailboxFolderException,
			MessageDeletionException;

	public void setMultipleDeletedFlags(int[] messageNumbers) throws MailboxFolderException,
			MessageDeletionException;

	public char getFolderSeparator() throws Exception;

	public TreeSet getAllFolders() throws Exception;

	public void createFolder(String folderName) throws MailboxFolderException;

	public void deleteFolder(String folderName) throws MailboxFolderException;

	public void moveMessages(int[] messageNumbers, String targetFolderName)
			throws MailboxFolderException, MessageMovementException;

}
