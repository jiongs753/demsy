package com.kmetop.demsy.modules.webmail.enums;

import java.util.HashMap;

public enum MailboxProtocolEnum {

	POP3("pop3", false), IMAP("imap", false), POP3_SSL("pop3s", true), IMAP_SSL("imaps", true);

	private static HashMap<String, MailboxProtocolEnum> byProtocolIdMap = new HashMap<String, MailboxProtocolEnum>();

	private String protocolId = null;

	private boolean useOfSsl = false;

	static {
		for (MailboxProtocolEnum mpe : MailboxProtocolEnum.values()) {
			byProtocolIdMap.put(mpe.protocolId, mpe);
		}
	}

	private MailboxProtocolEnum(String protocolId, boolean useOfSsl) {
		this.protocolId = protocolId;
		this.useOfSsl = useOfSsl;
	}

	public String getProtocolId() {
		return protocolId;
	}

	public boolean isUseOfSsl() {
		return useOfSsl;
	}

	public static MailboxProtocolEnum byProtocolId(String protocolId) {
		return (byProtocolIdMap.get(protocolId));
	}

}
