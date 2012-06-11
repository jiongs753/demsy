package com.kmetop.demsy.modules.webmail.enums;

public enum MailTransportProtocolEnum {
	SMTP("smtp"), SMTP_SSL("smtps");

	private static MailTransportProtocolEnum[] allValues = MailTransportProtocolEnum.values();

	private String protocolId = null;

	private MailTransportProtocolEnum(String protocolId) {
		this.protocolId = protocolId;
	}

	public String getProtocolId() {
		return protocolId;
	}

	public static MailTransportProtocolEnum byProtocolId(String protocolId) {
		for (int ii = 0; ii < allValues.length; ii++) {
			if (allValues[ii].protocolId.equals(protocolId)) {
				return (allValues[ii]);
			}
		}

		return (null);
	}

}
